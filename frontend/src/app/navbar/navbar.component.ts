import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { HttpEventType } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { AlertService } from '../service/alert.service';
import { AccountService } from '../service/account.service';
import { LoadingService } from '../service/loading.service';
import { PostService } from '../service/post.service';
import { AlertType } from '../enum/alert-type.enum';
import { User } from '../model/user';
import { Post } from '../model/post';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  user: User;
  searchedUser: User[];
  host: string;
  userHost: string;
  postHost: string;
  postPicture: File;
  userName: string;
  userLoggedIn: boolean;
  showNavbar: boolean;
  showSuccessAlert: boolean;
  photoName: string;
  latitude: any;
  longitude: any;
  location = null;
  progress: number;
  newPostURL: string;
  clientHost: string;
  postFail: boolean;

  constructor(
    private router: Router,
    private alertService: AlertService,
    private accountService: AccountService,
    private postService: PostService,
    private loadingService: LoadingService
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.host = this.postService.host;
    this.clientHost = this.postService.clientHost;
    this.userHost = this.postService.userHost;
    this.postHost = this.postService.postHost;
    this.showNavbar = true;
    if (this.accountService.isLoggedIn()) {
      this.userName = this.accountService.loggInUsername;
      this.getUserInfo(this.userName);
      this.getLonAndLat();
      this.loadingService.isLoading.next(false);
    } else {
      this.showNavbar = false;
      this.loadingService.isLoading.next(false);
    }
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.accountService.getUserInformation(username).subscribe(
      (response: User) => {
        this.user = response;
        this.userLoggedIn = true;
        this.showNavbar = true;
      },
      error => {
        console.log(error);
        this.userLoggedIn = false;
      }
    ));
  }

  onSearchUsers(event) {
    console.log(event);
    const username = event;
    this.subscriptions.push(this.accountService.searchUsers(username).subscribe(
      (response: User[]) => {
        console.log(response);
        this.searchedUser = response;
      },
      error => {
        console.log(error);
        return this.searchedUser = [];
      }
    ));
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  getSearchUserProfile(username: string): void {
    const element: HTMLElement = document.getElementById(
      'closeSearchModal'
    ) as HTMLElement;
    element.click();
    this.router.navigate(['/profile', username]);
    setTimeout(() => {
      location.reload();
    }, 100);
  }

  onFileSelected(event: any): void {
    console.log('file was seletected');
    this.postPicture = event.target.files[0];
    this.photoName = this.postPicture.name;
  }

  onNewPost(post: Post): void {
    const element: HTMLElement = document.getElementById(
      'dismissOnSubmitPost'
    ) as HTMLElement;
    element.click();
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.postService.save(post).subscribe(
        (response: Post) => {
          console.log(response);
          let postId: number = response.id;
          this.savePicture(this.postPicture);
          this.loadingService.isLoading.next(false);
          this.newPostURL = `${this.clientHost}/post/${postId}`;
        },
        error => {
          console.log(error);
          this.postFail = true;
          this.loadingService.isLoading.next(false);
        }
      )
    );
  }

  savePicture(picture: File): void {
    this.subscriptions.push(
      this.postService.uploadPostPicture(picture).subscribe(
        response => {
          if (response.type === HttpEventType.UploadProgress) {
            this.progress = (response.loaded / response.total) * 100;
          } else {
            console.log(response);
            this.OnNewPostSuccess(8);
          }
        },
        error => {
          console.log(error);
        }
      )
    );
  }

  OnNewPostSuccess(second: number): void{
    this.showSuccessAlert = true;
    setTimeout(() => {
      this.showSuccessAlert = false;
      this.newPostURL = null;
    }, second * 1000);
  }

  logOut(): void {
    this.loadingService.isLoading.next(true);
    this.accountService.logOut();
    this.router.navigateByUrl('/login');
    this.loadingService.isLoading.next(false);
    this.alertService.showAlert(
      'You have been successfully logged out.',
      AlertType.SUCCESS
    );
  }

  getLonAndLat(): void {
    if (window.navigator && window.navigator.geolocation) {
      window.navigator.geolocation.getCurrentPosition(
        position => {
          this.latitude = position.coords.latitude;
          this.longitude = position.coords.longitude;
          this.getUserLocation(this.latitude, this.longitude);
        },
        error => {
          switch (error.code) {
            case 1:
              console.log('Permission Location Denied.');
              break;
            case 2:
              console.log('Position Unavailable.');
              break;
            case 3:
              console.log('Timeout.');
              break;
          }
        }
      );
    }
  }

  getUserLocation(latitude, longitude): void {
    this.subscriptions.push(
      this.accountService.getLocation(latitude, longitude).subscribe(
        (response: any) => {
          this.location = response.results[3].formatted_address;
        },
        error => {
          console.log(error);
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe);
  }
}
