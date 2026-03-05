import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  form: FormGroup;
  loading = false;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  submit(): void {
    this.error = '';
    this.success = '';

    if (this.form.invalid) {
      this.error = 'Informe um email válido.';
      return;
    }

    const { email } = this.form.value;
    this.loading = true;

    this.auth.requestPasswordReset(email).subscribe({
      next: () => {
        this.loading = false;
        this.success = 'Se existir uma conta com este email, enviaremos instruções de redefinição.';
      },
      error: (err) => {
        this.loading = false;
        // Mesmo em erro, não revelamos se o email existe ou não
        this.success = 'Se existir uma conta com este email, enviaremos instruções de redefinição.';
        // eslint-disable-next-line no-console
        console.error('Forgot password error:', err);
      }
    });
  }
}

