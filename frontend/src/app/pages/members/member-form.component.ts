import { Component, OnInit, OnDestroy } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { InputTextModule } from "primeng/inputtext";
import { AutoCompleteModule } from "primeng/autocomplete";
import { DatePickerModule } from "primeng/datepicker";
import { IftaLabelModule } from "primeng/iftalabel";
import { ButtonModule } from "primeng/button";
import { InputMaskModule } from "primeng/inputmask";
import { Select } from "primeng/select";
import { MemberService } from "../../service/member.service";
import { PostcodeService } from "../../service/postcode.service";
import { Router, ActivatedRoute } from "@angular/router";
import { CommonModule } from "@angular/common";
import { Member } from "../../models/member.model";
import { Postcode } from "../../models/postcode.model";
import { debounceTime, Subject, Subscription } from "rxjs";

@Component({
  selector: "app-member-form",
  standalone: true,
  templateUrl: "./member-form.component.html",
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    InputMaskModule,
    AutoCompleteModule,
    DatePickerModule,
    IftaLabelModule,
    ButtonModule,
    Select,
  ],
})
export class MemberFormComponent implements OnInit, OnDestroy {
  member: Partial<Member> = {};
  postcodes: Postcode[] = [];
  filteredPostcodes: Postcode[] = [];
  selectedPostcode?: Postcode;
  isEdit = false;
  error = "";
  success = "";
  dateOfBirth: Date | null = null;
  icMask = "999999-99-9999";
  town: string = "";

  genderOptions = [
    { label: "Male", value: "Male" },
    { label: "Female", value: "Female" },
  ];

  // Debounce subjects and subscriptions
  private postcodeSearch$ = new Subject<string>();
  private icNumberChange$ = new Subject<string>();
  private subscriptions: Subscription[] = [];

  constructor(
    private memberService: MemberService,
    private postcodeService: PostcodeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Debounced postcode search
    this.subscriptions.push(
      this.postcodeSearch$.pipe(debounceTime(1000)).subscribe((query) => {
        this.postcodeService
          .getPostcodes({ search: query, pageSize: 10 })
          .subscribe((data) => {
            this.filteredPostcodes = data.postcodes;
          });
      })
    );

    // Debounced IC Number → DOB autofill
    this.subscriptions.push(
      this.icNumberChange$.pipe(debounceTime(1000)).subscribe((ic) => {
        this.setDateOfBirthFromIC(ic);
      })
    );

    // Load all postcodes for edit mode (to set selectedPostcode)
    this.postcodeService.getPostcodes({ pageSize: 1000 }).subscribe((data) => {
      this.postcodes = data.postcodes;
      if (this.isEdit && this.member.postcode) {
        this.selectedPostcode = this.postcodes.find(
          (p) => p.postcode === this.member.postcode
        );
        if (this.selectedPostcode) {
          this.town = this.selectedPostcode.town;
        }
      }
    });

    const id = this.route.snapshot.paramMap.get("id");
    if (id) {
      this.isEdit = true;
      this.memberService.getMember(+id).subscribe((m) => {
        this.member = { ...m };
        this.dateOfBirth = m.date_of_birth ? new Date(m.date_of_birth) : null;
        this.town = m.town || "";
        // selectedPostcode will be set after postcodes are loaded
      });
    }
  }

  ngOnDestroy() {
    this.subscriptions.forEach((s) => s.unsubscribe());
  }

  // IC Number input mask and DOB autofill (debounced)
  onIcNumberInput(value: string) {
    if (value) {
      this.icNumberChange$.next(value);
    }
  }

  setDateOfBirthFromIC(ic: string) {
    const value: string = ic.replace(/-/g, "");
    if (value.length >= 6) {
      // Parse YYMMDD
      const yy = value.substring(0, 2);
      const mm = value.substring(2, 4);
      const dd = value.substring(4, 6);
      // Guess century: if YY > current YY, assume 1900s, else 2000s
      const now = new Date();
      const currentYear = now.getFullYear() % 100;
      let fullYear = parseInt(yy, 10);
      fullYear += fullYear > currentYear ? 1900 : 2000;
      const dob = new Date(fullYear, parseInt(mm, 10) - 1, parseInt(dd, 10));
      if (!isNaN(dob.getTime())) {
        this.dateOfBirth = dob;
      }
    }
  }

  // Debounced postcode search
  filterPostcode(event: { query: string }) {
    this.postcodeSearch$.next(event.query);
  }

  // When postcode is selected, set town
  onPostcodeSelect(postcode: Postcode) {
    this.selectedPostcode = postcode;
    this.town = postcode.town;
  }

  onSubmit() {
    this.error = "";
    this.success = "";

    let dobString = "";
    if (this.dateOfBirth) {
      // Ensure this.dateOfBirth is a valid Date object
      const year = this.dateOfBirth.getFullYear();
      // getMonth() is 0-indexed (0 for January, 11 for December), so add 1
      const month = (this.dateOfBirth.getMonth() + 1)
        .toString()
        .padStart(2, "0");
      const day = this.dateOfBirth.getDate().toString().padStart(2, "0");
      dobString = `${year}-${month}-${day}`;
    }

    const payload: Member = {
      ...this.member,
      date_of_birth: dobString, // Use the manually constructed local date string
      join_date: new Date().toISOString().slice(0, 10), // For join_date, current UTC date might be acceptable
      postcode: this.selectedPostcode?.postcode || "",
      town: this.town || "",
    } as Member;

    if (this.isEdit && this.member.id) {
      this.memberService.updateMember(this.member.id, payload).subscribe({
        next: () => {
          this.success = "Member updated!";
          setTimeout(() => this.router.navigate(["/members"]), 1000);
        },
        error: (err) => (this.error = err.error?.message || "Update failed"),
      });
    } else {
      this.memberService.createMember(payload).subscribe({
        next: () => {
          this.success = "Member created!";
          setTimeout(() => this.router.navigate(["/members"]), 1000);
        },
        error: (err) => (this.error = err.error?.message || "Create failed"),
      });
    }
  }
}
