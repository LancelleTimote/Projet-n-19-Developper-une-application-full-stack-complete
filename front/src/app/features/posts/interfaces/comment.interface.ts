export interface Comment {
  id: number;
  content: string;
  username: string;
  createdAt: Date;
  post: { id: number };
  user: { id: number };
}
