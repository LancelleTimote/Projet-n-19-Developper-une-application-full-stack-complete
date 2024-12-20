export interface Post {
  id: number;
  title: string;
  content: string;
  topic_id: number;
  author_id: number;
  created_at: Date;
  updated_at: Date;
}
