import { Post } from './post';

export class User {
  id: number;
  name: string;
  username: string;
  email: string;
  password: string;
  bio: string;
  postCount: number;
  post: Post[];
  likedPost: Post[];
  creationDate: Date;
}
