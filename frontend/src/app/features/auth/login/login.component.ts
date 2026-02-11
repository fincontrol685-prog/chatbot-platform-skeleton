import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  form: FormGroup;
  error = '';

  constructor(private auth: AuthService, private router: Router, fb: FormBuilder) {
    this.form = fb.group({ username: ['', Validators.required], password: ['', Validators.required] });
  }

  submit() {
    this.error = '';
    if (this.form.invalid) { this.error = 'Preencha usuário e senha'; return; }
    const { username, password } = this.form.value;
    this.auth.login(username, password).subscribe({
      next: () => this.router.navigate(['/bots']),
      error: (err) => { this.error = 'Login failed'; console.error(err); }
    });
  }
}
