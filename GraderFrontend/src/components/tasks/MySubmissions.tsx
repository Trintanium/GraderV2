import { submissionDto } from "@/types/dto";

interface MySubmissionsProps {
  submission: submissionDto[];
}

export default function MySubmissions({ submission }: MySubmissionsProps) {
  return (
    <div className="flex flex-col flex-1">
      <h2 className="text-xl font-bold mb-3">My Submissions</h2>
      {submission.length === 0 ? (
        <div>No submissions yet</div>
      ) : (
        <div className="flex-1 overflow-auto border border-[#746F6F] rounded p-2">
          <ul className="list-disc space-y-3">
            {submission.map((s) => (
              <li key={s.id}>
                <div>Language: {s.language}</div>
                <div>Score: {s.score}</div>
                <div>
                  Status:{" "}
                  <span
                    className={`font-bold ${
                      s.status === "ACCEPTED"
                        ? "text-green-600"
                        : s.status === "FAILED"
                        ? "text-red-600"
                        : "text-yellow-600"
                    }`}
                  >
                    {s.status}
                  </span>
                </div>
                <div>
                  Submitted At:{" "}
                  {s.submittedAt
                    ? new Date(s.submittedAt).toLocaleString()
                    : "N/A"}
                </div>
                <hr className="my-2 border-[#746F6F] border-t" />
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
