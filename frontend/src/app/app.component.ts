import { BreakpointObserver } from '@angular/cdk/layout';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, DestroyRef, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, map, startWith } from 'rxjs/operators';
import { AuthService } from './core/auth.service';

interface NavigationItem {
  label: string;
  caption: string;
  icon: string;
  route: string;
  exact?: boolean;
}

interface NavigationSection {
  label: string;
  items: NavigationItem[];
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  private readonly auth = inject(AuthService);
  readonly session$ = this.auth.session$;
  readonly navigation: NavigationSection[] = [
    {
      label: 'Operacao',
      items: [
        {
          label: 'Dashboard',
          caption: 'Visao executiva e desempenho geral',
          icon: 'dashboard',
          route: '/dashboard',
          exact: true
        },
        {
          label: 'Bots',
          caption: 'Catalogo, configuracoes e status',
          icon: 'smart_toy',
          route: '/bots',
          exact: false
        },
        {
          label: 'Conversas',
          caption: 'Fluxos ativos e historico de interacoes',
          icon: 'forum',
          route: '/conversations',
          exact: false
        }
      ]
    },
    {
      label: 'Governanca',
      items: [
        {
          label: 'Usuarios',
          caption: 'Perfis, papeis e acessos operacionais',
          icon: 'badge',
          route: '/professional/users',
          exact: false
        },
        {
          label: 'Estrutura',
          caption: 'Departamentos, equipes e ownership',
          icon: 'apartment',
          route: '/professional/departments',
          exact: false
        },
        {
          label: 'Seguranca',
          caption: 'Consentimento, 2FA e conformidade',
          icon: 'verified_user',
          route: '/compliance/security',
          exact: false
        },
        {
          label: 'Monitoramento',
          caption: 'Metricas operacionais e performance por bot',
          icon: 'monitoring',
          route: '/analytics-advanced/dashboard',
          exact: false
        },
        {
          label: 'Auditoria',
          caption: 'Rastreabilidade e eventos criticos',
          icon: 'history_edu',
          route: '/audit-logs',
          exact: false
        }
      ]
    }
  ];
  readonly currentYear = new Date().getFullYear();

  isCompact = false;
  drawerOpened = true;
  pageTitle = 'Dashboard';
  pageDescription = 'Monitore a plataforma e acompanhe os fluxos mais importantes.';

  private readonly destroyRef = inject(DestroyRef);

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private breakpointObserver: BreakpointObserver,
    private cdr: ChangeDetectorRef
  ) {
    this.breakpointObserver.observe('(max-width: 1100px)')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(state => {
        this.isCompact = state.matches;
        this.drawerOpened = !this.isCompact;
        this.cdr.markForCheck();
      });

    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        startWith(null),
        map(() => this.resolveRouteData(this.activatedRoute)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(routeData => {
        this.pageTitle = routeData.title;
        this.pageDescription = routeData.description;
        if (this.isCompact) {
          this.drawerOpened = false;
        }
        this.cdr.markForCheck();
      });
  }

  logout(): void {
    this.auth.logout();
    void this.router.navigate(['/login']);
  }

  toggleNavigation(): void {
    if (this.isCompact) {
      this.drawerOpened = !this.drawerOpened;
      this.cdr.markForCheck();
    }
  }

  closeNavigation(): void {
    if (this.isCompact) {
      this.drawerOpened = false;
      this.cdr.markForCheck();
    }
  }

  private resolveRouteData(route: ActivatedRoute): { title: string; description: string } {
    let currentRoute: ActivatedRoute | null = route;
    let resolvedTitle = 'Workspace';
    let resolvedDescription = 'Acompanhe sua operacao e gerencie a plataforma.';

    while (currentRoute) {
      const routeTitle = currentRoute.snapshot.data['title'];
      const routeDescription = currentRoute.snapshot.data['description'];

      if (routeTitle) {
        resolvedTitle = routeTitle;
      }

      if (routeDescription) {
        resolvedDescription = routeDescription;
      }

      currentRoute = currentRoute.firstChild;
    }

    return {
      title: resolvedTitle,
      description: resolvedDescription
    };
  }
}
