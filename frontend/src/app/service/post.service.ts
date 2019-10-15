import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, VirtualTimeScheduler } from 'rxjs';
import { Post } from '../model/post';
import { Comment } from '../model/comment';
import { ServerConstant } from '../constant/server-constant';
import { NavbarComponent } from '../navbar/navbar.component';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constant: ServerConstant = new ServerConstant();
  public host = this.constant.host;
  public clientHost = this.constant.client;
  public userHost = this.constant.host;
  public postHost = this.constant.postPicture;

  constructor(private http: HttpClient) { }

  save(post: Post): Observable<Post> {
    return this.http.post<Post>(`${this.host}/posts`, post);
  }

  getOnePostById(postId: number): Observable<Post> {
    return this.http.get<Post>(`${this.host}/posts/${postId}`);
  }

  getPostsByUsername(username: string): Observable<any> {
    return this.http.get<any>(`${this.host}/posts?username=${username}`);
  }

  saveComment(comment: Comment, postId: number): Observable<Comment> {
    return this.http.post<Comment>(`${this.host}/posts/${postId}/comments`, comment);
  }

  delete(postId: number): Observable<Post> {
    return this.http.delete<Post>(`${this.host}/posts/${postId}`);
  }

  like(postId: number) {
    return this.http.put(`${this.host}/posts/${postId}/like`, {
      responseType: 'text'
    });
  }

  unLike(postId: number) {
    return this.http.put(`${this.host}/posts/${postId}/unlike`, {
      responseType: 'text'
    });
  }

uploadPostPicture(recipePicture: File, postId: number) {
  const fd = new FormData();
  fd.append('image', recipePicture, recipePicture.name);
  return this.http.post(`${this.host}/posts/${postId}/upload`, fd, {
    responseType: 'text',
    reportProgress: true,
    observe: 'events'});
}

}
