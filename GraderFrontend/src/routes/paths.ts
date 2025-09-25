export const PATHS = {
  HOME: "/",

  TASKS: {
    ROOT: "tasks",
    NEW: "tasks/new",
    DETAILS: "tasks/:taskId",
    EDIT: "tasks/:taskId/edit",

    TESTCASES: {
      ROOT: "tasks/:taskId/testcases",
      NEW: "tasks/:taskId/testcases/new",
      DETAILS: "tasks/:taskId/testcases/:testcaseId",
      EDIT: "tasks/:taskId/testcases/:testcaseId/edit",
    },
  },

  TAGS: {
    ROOT: "tags",
    // NEW: "tags/new",
    // DETAILS: "tags/:tagId",
    // EDIT: "tags/:tagId/edit",
  },

  AUTH: "auth",
  EMAIL_VERIFY: "email/verify",

  PASSWORD: {
    FORM: "password/form",
    RESET: "password/reset",
  },

  SETTINGS: "settings",
  ABOUT: "about",
  NOT_FOUND: "*",
};
