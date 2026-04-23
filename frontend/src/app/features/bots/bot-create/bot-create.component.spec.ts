import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BotCreateComponent } from './bot-create.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../material.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { BotService } from '../bot.service';

class BotServiceStub {
  create() { return of({}); }
  update() { return of({}); }
  get() { return of({ id: 1, name: 'Bot', key: 'bot', enabled: true, config: null }); }
}

describe('BotCreateComponent', () => {
  let component: BotCreateComponent;
  let fixture: ComponentFixture<BotCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BotCreateComponent],
      imports: [ReactiveFormsModule, MaterialModule, NoopAnimationsModule, RouterTestingModule],
      providers: [{ provide: BotService, useClass: BotServiceStub }]
    }).compileComponents();
    fixture = TestBed.createComponent(BotCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => { expect(component).toBeTruthy(); });
});
