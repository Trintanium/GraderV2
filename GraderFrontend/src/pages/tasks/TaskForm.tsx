import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  fetchData,
  createFormData,
  updateFormData,
  createData,
  deleteData,
} from "@/libs/fetchUtils";
import { problemDto, tagDto, problemTagDto } from "@/types/dto";

export default function TaskForm() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const isEdit = !!id && !isNaN(Number(id));

  const [title, setTitle] = useState("");
  const [difficulty, setDifficulty] = useState("");
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [file, setFile] = useState<File | null>(null);

  // Fetch all tags
  const { data: tags } = useQuery<tagDto[]>({
    queryKey: ["tags"],
    queryFn: () => fetchData("/tag"),
  });

  // Fetch problem data if editing
  const { data: problem } = useQuery<problemDto>({
    queryKey: ["problem", id],
    queryFn: () => fetchData(`/problem/${id}`),
    enabled: isEdit,
  });

  // Fetch problem tags if editing
  const { data: problemTags } = useQuery<tagDto[]>({
    queryKey: ["problemTags", id],
    queryFn: () => fetchData(`/problem/${id}/tags`),
    enabled: isEdit,
  });

  useEffect(() => {
    if (problem) {
      setTitle(problem.title);
      setDifficulty(problem.difficulty);
    }
  }, [problem]);

  useEffect(() => {
    if (problemTags) {
      setSelectedTags(problemTags.map((tag) => tag.id));
    }
  }, [problemTags]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleTagToggle = (tagId: number) => {
    setSelectedTags((prev) =>
      prev.includes(tagId)
        ? prev.filter((id) => id !== tagId)
        : [...prev, tagId]
    );
  };

  const saveMutation = useMutation({
    mutationFn: async () => {
      const form = new FormData();
      form.append("title", title);
      form.append("difficulty", difficulty);
      if (file) form.append("pdf", file);

      const savedProblem = isEdit
        ? await updateFormData<problemDto>(`/problem/${id}`, form)
        : await createFormData<problemDto>("/problem", form);

      const problemId = savedProblem.id;

      const oldProblemTags = await fetchData<problemTagDto[]>(
        `/problem-tag?problemId=${problemId}`
      );
      const oldTagIds = oldProblemTags.map((t) => t.tagId);

      // Tags to remove from this problem
      const tagsToDelete = oldProblemTags.filter(
        (t) => !selectedTags.includes(t.tagId)
      );

      await Promise.all(
        tagsToDelete.map((t) => deleteData(`/problem-tag/${t.id}`))
      );

      // Tags to add to this problem
      const tagsToAdd = selectedTags.filter((id) => !oldTagIds.includes(id));

      await Promise.all(
        tagsToAdd.map((tagId) =>
          createData<problemTagDto, Partial<problemTagDto>>(`/problem-tag`, {
            problemId,
            tagId,
          })
        )
      );

      return savedProblem;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["problems"] });
      queryClient.invalidateQueries({ queryKey: ["problemTags"] });
      navigate("/tasks");
    },
  });
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    saveMutation.mutate();
  };

  return (
    <div className="max-w-md mx-auto p-4 bg-white rounded shadow">
      <Link
        to="/tasks"
        className="self-start px-2 py-1 bg-blue-500 text-white rounded"
      >
        Back
      </Link>
      <h2 className="text-xl font-bold mb-4">
        {isEdit ? "Edit Problem" : "Create Problem"}
      </h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Title */}
        <div>
          <label className="block mb-1 font-medium">Title</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border rounded px-2 py-1"
            required
          />
        </div>

        {/* Difficulty */}
        <div>
          <label className="block mb-1 font-medium">Difficulty</label>
          <select
            value={difficulty}
            onChange={(e) => setDifficulty(e.target.value)}
            className="w-full border rounded px-2 py-1"
            required
          >
            <option value="">Select difficulty</option>
            <option value="EASY">Easy</option>
            <option value="MEDIUM">Medium</option>
            <option value="HARD">Hard</option>
          </select>
        </div>

        {/* Tags */}
        <div>
          <label className="block mb-1 font-medium">Tags</label>
          <div className="flex flex-wrap gap-2">
            {tags?.map((tag) => (
              <button
                type="button"
                key={tag.id}
                onClick={() => handleTagToggle(tag.id)}
                className={`px-3 py-1 rounded border ${
                  selectedTags.includes(tag.id)
                    ? "bg-blue-500 text-white border-blue-500"
                    : "bg-white text-gray-700 border-gray-300"
                }`}
              >
                {tag.name}
              </button>
            ))}
          </div>
        </div>

        {/* PDF */}
        <div>
          <label className="block mb-1 font-medium">PDF</label>
          <input type="file" accept=".pdf" onChange={handleFileChange} />
          {problem?.pdfUrl && !file && (
            <p className="mt-1 text-sm text-gray-500">
              Current PDF:{" "}
              <a
                href={problem.pdfUrl}
                target="_blank"
                rel="noreferrer"
                className="text-blue-500 underline"
              >
                View
              </a>
            </p>
          )}
        </div>

        {/* Submit */}
        <div>
          <button
            type="submit"
            className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600"
            disabled={saveMutation.isPending}
          >
            {saveMutation.isPending
              ? "Saving..."
              : isEdit
              ? "Update Problem"
              : "Create Problem"}
          </button>
        </div>
      </form>
    </div>
  );
}
