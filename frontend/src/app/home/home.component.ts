import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../service/account.service';
import { LoadingService } from '../service/loading.service';
import { AlertService } from '../service/alert.service';
import { PostService } from '../service/post.service';
import { Subscription } from 'rxjs';
import { AlertType } from '../enum/alert-type.enum';
import { Post } from '../model/post';
import { User } from '../model/user';
import { Comment } from '../model/comment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  user = new User();
  posts: Post[] = [];
  host: string;
  userHost: string;
  postHost: string;
  comment: Comment = new Comment();
  color: string;
  like: string;
  post: Post = new Post();

  constructor(
    private router: Router,
    private accountService: AccountService,
    private postService: PostService,
    private loadingService: LoadingService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.accountService.isLoggedIn();
    this.getUserInfo(this.accountService.loggInUsername);
    this.getPosts();
    this.host = this.postService.host;
    this.userHost = this.postService.host;
    this.postHost = this.postService.host;
    this.loadingService.isLoading.next(false);
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.accountService.getUserInformation(username).subscribe(
      (response: User) => {
        this.user = response;
      },
      error => {
        console.log(error);
        this.user = null;
        this.logOut();
        this.router.navigateByUrl('/login');
      }
    ));
  }

  logOut(): void {
    this.accountService.logOut();
    this.router.navigateByUrl('/login');
    this.alertService.showAlert(
      'You need to log in to access this page.',
      AlertType.DANGER
    );
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  getPosts(): void {
    this.subscriptions.push(this.accountService.getPosts().subscribe(
      (response: any) => {
        this.posts = response['content'];
        this.loadingService.isLoading.next(false);
      },
      error => {
        console.log(error);
        this.loadingService.isLoading.next(false);
      }
    ));
  }

  onDelete(id: number): void {
    this.subscriptions.push(
      this.postService.delete(id).subscribe(
      response => {
        this.alertService.showAlert(
          'Post was deleted successfully.',
          AlertType.SUCCESS
        );
        this.getPosts();
      },
      error => {
        console.log(error);
        this.alertService.showAlert(
          'Post was not deleted. Please try again.',
          AlertType.DANGER
        );
        this.getPosts();
      }
    ));
  }

  seeOnePost(postId): void {
    this.router.navigate(['/post', postId]);
    console.log(postId);
  }

  onAddComment(comment, post: Post) {
    console.log(comment);
    this.comment.content = '';
    const newComment: Comment = new Comment();
    newComment.content = comment.value.content;
    newComment.postId = comment.value.postId;
    newComment.creationDate = new Date();
    newComment.username = comment.value.username;
    console.log(newComment);
    post.comments.push(newComment);
    this.subscriptions.push(
      this.postService.saveComment(newComment, newComment.postId).subscribe()
    );
  }

  doLike(post) {
    post.likesCount+=1;
    post.liked=true;
    this.subscriptions.push(
      this.postService.like(post.id).subscribe()
    );
  }

  doUnlike(post) {
    if(post.likesCount > 0)
      post.likesCount-=1;
    post.liked=false;
    this.subscriptions.push(
      this.postService.unLike(post.id).subscribe()
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
