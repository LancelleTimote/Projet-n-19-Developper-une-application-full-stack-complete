import { AbstractControl, ValidationErrors } from '@angular/forms';

export function usernameValidator(
  control: AbstractControl
): ValidationErrors | null {
  const username = control.value;
  const usernameRegex = /^[a-zA-Z0-9]{3,12}$/;

  return usernameRegex.test(username)
    ? null
    : { invalidUsername: true };
}
