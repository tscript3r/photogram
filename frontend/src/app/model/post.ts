import { Comment } from './comment';

export class Post {

  public id: number;
  public username: string;
  public caption: string;
  public creationDate: Date;
  public location: string;
  public likesCount: number;
  public dislikesCount: number;
  public liked: boolean;
  public disliked: boolean;
  public userId: number;
  public comments: Comment[];

}
