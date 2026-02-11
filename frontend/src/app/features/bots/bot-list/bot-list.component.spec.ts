import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BotListComponent } from './bot-list.component';
import { MaterialModule } from '../../../material.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { BotService } from '../bot.service';

class BotServiceStub { list() { return of([]); } activate() { return of({}); } }

describe('BotListComponent', () => {
  let component: BotListComponent;
  let fixture: ComponentFixture<BotListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BotListComponent],
      imports: [MaterialModule, NoopAnimationsModule],
      providers: [{ provide: BotService, useClass: BotServiceStub }]
    }).compileComponents();
    fixture = TestBed.createComponent(BotListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => { expect(component).toBeTruthy(); });
});
