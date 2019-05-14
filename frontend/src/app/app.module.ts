import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

// components
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { NavbarComponent } from './navbar/navbar.component';
import { PostDetailComponent } from './post-detail/post-detail.component';

// services
import { AlertService } from './service/alert.service';
import { AccountService } from './service/account.service';
import { LoadingService } from './service/loading.service';
import { PostService } from './service/post.service';
import { PostresolverService } from './service/postresolver.service';

// guards
import { AuthenticationGuard } from './guard/authentication.guard';

// interceptors
import { CacheInterceptor } from './interceptor/cache.interceptor';
import { AuthInterceptor } from './interceptor/auth.interceptor';

// external modules
import { NgxLoadingModule } from 'ngx-loading';


const appRoutes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'signup', component: SignupComponent },
  { path: 'resetpassword', component: ResetPasswordComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard] },
  { path: 'post/:postId', component: PostDetailComponent,
   resolve: {resolvedPost: PostresolverService}, canActivate: [AuthenticationGuard] },
  { path: 'profile/:username', component: ProfileComponent, canActivate: [AuthenticationGuard] },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    ResetPasswordComponent,
    HomeComponent,
    ProfileComponent,
    NavbarComponent,
    PostDetailComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(appRoutes),
    NgxLoadingModule.forRoot({})
  ],
  providers: [
    AccountService,
    LoadingService,
    PostService,
    AlertService,
    PostresolverService,
    AuthenticationGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: CacheInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
