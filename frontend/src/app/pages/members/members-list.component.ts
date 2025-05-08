import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";
import { ButtonModule } from "primeng/button";
import { PaginatorModule, PaginatorState } from "primeng/paginator";
import { MemberService } from "../../service/member.service";
import { Member } from "../../models/member.model";
import { Router } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { InputTextModule } from "primeng/inputtext";

@Component({
  selector: "app-members-list",
  standalone: true,
  templateUrl: "./members-list.component.html",
  imports: [
    CommonModule,
    TableModule,
    TagModule,
    ButtonModule,
    PaginatorModule,
    FormsModule,
    InputTextModule,
  ],
})
export class MembersListComponent implements OnInit {
  members: Member[] = [];
  totalRecords = 0;
  first = 0;
  rows = 10;
  search = "";
  loading = false;

  // Sorting state
  sortField: string = "join_date";
  sortOrder: "asc" | "desc" = "desc";

  constructor(private memberService: MemberService, private router: Router) {}

  ngOnInit() {
    this.loadMembers();
  }

  loadMembers() {
    this.loading = true;
    this.memberService
      .getMembers({
        pageIndex: Math.floor(this.first / this.rows),
        pageSize: this.rows,
        search: this.search,
        sortField: this.sortField,
        sortOrder: this.sortOrder,
      })
      .subscribe({
        next: (res) => {
          this.members = res.members;
          this.totalRecords = res.totalCount;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        },
      });
  }

  onPageChange(event: PaginatorState) {
    this.first = event.first ?? 0;
    this.rows = event.rows ?? 10;
    this.loadMembers();
  }

  onSearch() {
    this.first = 0;
    this.loadMembers();
  }

  onSort(field: string) {
    if (this.sortField === field) {
      // Toggle sort order
      this.sortOrder = this.sortOrder === "asc" ? "desc" : "asc";
    } else {
      this.sortField = field;
      this.sortOrder = "asc";
    }
    this.loadMembers();
  }

  editMember(member: Member) {
    this.router.navigate(["/members/edit", member.id]);
  }

  addMember() {
    this.router.navigate(["/members/add"]);
  }

  deleteMember(member: Member) {
    if (confirm("Are you sure you want to delete this member?")) {
      this.memberService
        .deleteMember(member.id!)
        .subscribe(() => this.loadMembers());
    }
  }
}
