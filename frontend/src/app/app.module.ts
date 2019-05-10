import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgxLoadingModule } from 'ngx-loading';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { SingupComponent } from './components/singup/singup.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { PostDetailsComponent } from './components/post-details/post-details.component';
import { AuthenticationGuard } from './guard/authentication.guard';
import { AccountService } from './services/account.service';
import { LoadingService } from './services/loading.service';
import { AlertService } from './services/alert.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { CacheInterceptor } from './interceptors/cache.interceptor';
import { PostResolverService } from './services/post-resolver-service';
import { RouterModule, Routes } from '@angular/router';

const appRoutes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'singup', component: SingupComponent},
  {path: 'resetpassword', component: ResetPasswordComponent},
  {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
  {path: 'post/:postId', component: PostDetailsComponent, resolve: {resolvedPost: PostResolverService}},
  {path: 'profile/:username', component: ProfileComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SingupComponent,
    ResetPasswordComponent,
    HomeComponent,
    ProfileComponent,
    NavbarComponent,
    PostDetailsComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    NgxLoadingModule.forRoot({})
  ],
  providers: [
    AccountService,
    LoadingService,
    AlertService,
    PostResolverService,
    AuthenticationGuard,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: CacheInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
