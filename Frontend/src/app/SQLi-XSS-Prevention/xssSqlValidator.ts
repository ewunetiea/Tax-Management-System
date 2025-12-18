import { AbstractControl, ValidationErrors } from '@angular/forms';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';

export function xssSqlValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (!value) return null;

  return InputSanitizer.isInvalid(value) ? { xssSqlDetected: true } : null;
}
