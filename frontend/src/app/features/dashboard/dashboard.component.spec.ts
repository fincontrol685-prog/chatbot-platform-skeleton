import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { DashboardService } from './dashboard.service';
import { RouterTestingModule } from '@angular/router/testing';

class DashboardServiceStub {
  getStats() {
    return of({
      botCount: 0,
      activeConversationCount: 0,
      totalMessageCount: 0,
      userCount: 0
    });
  }
}

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent, NoopAnimationsModule, RouterTestingModule],
      providers: [{ provide: DashboardService, useClass: DashboardServiceStub }]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
