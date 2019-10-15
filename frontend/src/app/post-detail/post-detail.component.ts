import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { AlertService } from '../service/alert.service';
import { AccountService } from '../service/account.service';
import { PostService } from '../service/post.service';
import { LoadingService } from '../service/loading.service';
import { AlertType } from '../enum/alert-type.enum';
import { User } from '../model/user';
import { Post } from '../model/post';
import { Comment } from '../model/comment';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {
  private subscriptions: Subscription[] = [];
  user = new User();
  loggedUsername: string;
  posts: Post[] = [];
  host: string;
  userHost: string;
  postHost: string;
  userName: string;
  comment: Comment = new Comment();
  commentList: Array<object> = [];
  post: Post = new Post();
  like: string;
  isUser: boolean;
  postId: number;
  color: string;

  constructor(
    public accountService: AccountService,
    private postService: PostService,
    private router: Router,
    private route: ActivatedRoute,
    private loadingService: LoadingService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.comment.content = '';
    this.resolvePost();
  }

  resolvePost(): void {
    const resolvedPost: Post = this.route.snapshot.data.resolvedPost;
    if (resolvedPost != null) {
      this.post = resolvedPost;
      this.userHost = this.postService.userHost;
      this.postHost = this.postService.userHost;
      this.host = this.postService.host;
      this.accountService.isLoggedIn();
      this.loggedUsername = this.accountService.loggInUsername;
      this.getUserInfo(resolvedPost.userId);
      this.loadingService.isLoading.next(false);
    } else {
      this.loadingService.isLoading.next(false);
      this.alertService.showAlert('Post was not found.', AlertType.DANGER);
      this.router.navigateByUrl('/home');
    }
  }

  getUserInfo(id: number): void {
    this.subscriptions.push(
      this.accountService.getUserById(id).subscribe(
        (response: User) => {
          this.user = response;
        },
        error => {
          console.log(error);
        }
      )
    );
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  onDelete(id: number) {
    this.subscriptions.push(
      this.postService.delete(id).subscribe(
        response => {
          console.log('The deleted post: ', response);
          this.alertService.showAlert(
            'Post was deleted successfully.',
            AlertType.SUCCESS
          );
          this.router.navigateByUrl('/home');
        },
        error => {
          console.log(error);
          this.alertService.showAlert(
            'Post was not deleted. Please try again.',
            AlertType.DANGER
          );
        }
      )
    );
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

}
