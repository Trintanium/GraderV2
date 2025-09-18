export interface tagDto {
  id: number;
  name: string;
}

export interface problemDto {
  id: number;
  title: string;
  difficulty: string;
  pdf: string;
  pdfUrl: string;
}

export interface problemTagDto {
  id: number;
  tagId: number;
  problemId: number;
}

export interface submissionDto {
  id: number;
  userId: number;
  problemId: number;
  code: string;
  score: number;
  language: string;
  status: string;
  submittedAt: string;
}

export interface UserDto {
  id: number;
  username: string;
  email: string;
  password: string;
  role: string;
  profilePicture: string;
  createdAt: string;
  updatedAt: string;
  emailVerified: boolean;
}

export interface TestCaseDto {
  id: number;
  problemId: number;
  input: string;
  output: string;
  type: string;
}
