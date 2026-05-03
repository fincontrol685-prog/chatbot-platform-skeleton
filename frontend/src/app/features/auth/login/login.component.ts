import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { getApiErrorMessage } from '../../../core/api-error.util';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  error = '';
  loading = false;
  showPassword = false;
  private returnUrl = '/dashboard';

  constructor(
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';

    if (this.auth.isAuthenticated()) {
      void this.router.navigateByUrl(this.returnUrl);
      return;
    }
  }

  submit(): void {
    this.error = '';
    if (this.form.invalid) {
      this.error = 'Preencha usuário e senha';
      return;
    }
    this.loading = true;
    const { username, password } = this.form.value;
    this.auth.login(username, password).subscribe({
      next: () => {
        this.loading = false;
        void this.router.navigateByUrl(this.returnUrl);
      },
      error: (err) => {
        this.loading = false;
        this.error = getApiErrorMessage(err, 'Falha ao fazer login. Verifique as credenciais.');
        console.error('Login error:', err);
      }
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}
