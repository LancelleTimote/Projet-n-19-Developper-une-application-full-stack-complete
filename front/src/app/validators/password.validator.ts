import { AbstractControl, ValidationErrors } from '@angular/forms';

export function passwordValidator(
  control: AbstractControl
): ValidationErrors | null {
  const password = control.value;
  const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

  return passwordRegex.test(password) ? null : { invalidPassword: true };
}
