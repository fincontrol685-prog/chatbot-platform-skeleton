import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BotCreateComponent } from './bot-create.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../material.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('BotCreateComponent', () => {
  let component: BotCreateComponent;
  let fixture: ComponentFixture<BotCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({ declarations: [BotCreateComponent], imports: [ReactiveFormsModule, MaterialModule, NoopAnimationsModule] }).compileComponents();
    fixture = TestBed.createComponent(BotCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => { expect(component).toBeTruthy(); });
});
