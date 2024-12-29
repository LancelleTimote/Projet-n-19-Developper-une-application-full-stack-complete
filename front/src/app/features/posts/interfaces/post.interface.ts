export interface Post {
  id: number;
  title: string;
  content: string;
  topic: { id: number; name?: string }; // Relation avec Topic
  author: { id: number; username?: string }; // Relation avec User
  created_at: Date;
  updated_at: Date;
}
