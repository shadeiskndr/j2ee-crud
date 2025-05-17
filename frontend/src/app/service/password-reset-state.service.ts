import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

export interface PasswordResetState {
  email?: string;
  resetToken?: string;
  step: "initiate" | "verify" | "reset" | "complete";
}

@Injectable({
  providedIn: "root",
})
export class PasswordResetStateService {
  private stateSubject = new BehaviorSubject<PasswordResetState>({
    step: "initiate",
  });

  public state$ = this.stateSubject.asObservable();

  constructor() {}

  updateState(newState: Partial<PasswordResetState>) {
    const currentState = this.stateSubject.value;
    this.stateSubject.next({ ...currentState, ...newState });
  }

  resetState() {
    this.stateSubject.next({ step: "initiate" });
  }

  getCurrentState(): PasswordResetState {
    return this.stateSubject.value;
  }

  setEmail(email: string) {
    this.updateState({ email, step: "verify" });
  }

  setResetToken(resetToken: string) {
    this.updateState({ resetToken, step: "reset" });
  }

  completeReset() {
    this.updateState({ step: "complete" });
    // Clear sensitive data
    setTimeout(() => {
      this.resetState();
    }, 3000);
  }
}
