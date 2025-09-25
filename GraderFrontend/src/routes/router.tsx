// src/routes/router.tsx
import { createBrowserRouter } from "react-router-dom";
import { PATHS } from "./paths";

import App from "@/App";
import Home from "@/pages/Home";
import Tasks from "@/pages/tasks/Tasks";
import TaskForm from "@/pages/tasks/TaskForm";
import TaskDetails from "@/pages/tasks/TaskDetails";
import Tags from "@/pages/tags/Tags";

import TestCase from "@/pages/testCases/TestCase";
import TestcaseForm from "@/pages/testCases/TestCaseForm";
import About from "@/pages/About";
import AuthPage from "@/pages/auth/AuthPage";
import EmailVerify from "@/pages/auth/EmailVerify";
import ResetPasswordForm from "@/pages/auth/ResetPasswordForm";
import ResetPassword from "@/pages/auth/ResetPassword";
import UserSetting from "@/pages/user/UserSetting";

export const router = createBrowserRouter([
  {
    path: PATHS.HOME,
    element: <App />,
    children: [
      { index: true, element: <Home /> },

      {
        path: PATHS.TASKS.ROOT,
        children: [
          { index: true, element: <Tasks /> },
          { path: "new", element: <TaskForm /> },
          { path: ":taskId", element: <TaskDetails /> },
          { path: ":taskId/testcases", element: <TestCase /> },
          { path: ":taskId/testcases/new", element: <TestcaseForm /> },
          {
            path: ":taskId/testcases/:testcaseId/edit",
            element: <TestcaseForm />,
          },
          { path: ":taskId/edit", element: <TaskForm /> },
        ],
      },

      { path: PATHS.TAGS.ROOT, element: <Tags /> },
      { path: PATHS.AUTH, element: <AuthPage /> },
      { path: PATHS.EMAIL_VERIFY, element: <EmailVerify /> },

      {
        path: "password",
        children: [
          { path: "form", element: <ResetPasswordForm /> },
          { path: "reset", element: <ResetPassword /> },
        ],
      },

      { path: PATHS.SETTINGS, element: <UserSetting /> },
      { path: PATHS.ABOUT, element: <About /> },
      { path: PATHS.NOT_FOUND, element: <div>Page Not Found</div> },
    ],
  },
]);
