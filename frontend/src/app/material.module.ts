import { NgModule } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';

@NgModule({
  imports: [MatToolbarModule, MatButtonModule, MatInputModule, MatFormFieldModule, MatListModule, MatCardModule, MatIconModule, MatTableModule],
  exports: [MatToolbarModule, MatButtonModule, MatInputModule, MatFormFieldModule, MatListModule, MatCardModule, MatIconModule, MatTableModule]
})
export class MaterialModule { }
