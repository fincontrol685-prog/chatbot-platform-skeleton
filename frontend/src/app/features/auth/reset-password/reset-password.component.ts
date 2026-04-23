import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/auth.service';
import { getApiErrorMessage } from '../../../core/api-error.util';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  form: FormGroup;
  loading = false;
  error = '';
  success = '';
  token = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthService
  ) {
    this.form = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token') || '';
  }

  submit(): void {
    this.error = '';
    this.success = '';

    if (!this.token) {
      this.error = 'Token de redefinição inválido.';
      return;
    }

    if (this.form.invalid) {
      this.error = 'Preencha a nova senha corretamente.';
      return;
    }

    const { password, confirmPassword } = this.form.value;
    if (password !== confirmPassword) {
      this.error = 'As senhas não conferem.';
      return;
    }

    this.loading = true;
    this.auth.resetPassword(this.token, password).subscribe({
      next: () => {
        this.loading = false;
        this.success = 'Senha redefinida com sucesso! Você já pode fazer login.';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.error = getApiErrorMessage(err, 'Token invalido ou expirado.');
        // eslint-disable-next-line no-console
        console.error('Reset password error:', err);
      }
    });
  }
}
