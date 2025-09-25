import { useState, useEffect, useRef } from "react";
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
  const { taskId } = useParams<{ taskId: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const isEdit = !!taskId && !isNaN(Number(taskId));

  const [title, setTitle] = useState("");
  const [difficulty, setDifficulty] = useState("");
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [file, setFile] = useState<File | null>(null);
  const [pdfPreview, setPdfPreview] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  // Fetch all tags
  const { data: tags } = useQuery<tagDto[]>({
    queryKey: ["tags"],
    queryFn: () => fetchData("/tag"),
  });

  // Fetch problem data if editing
  const { data: problem } = useQuery<problemDto>({
    queryKey: ["problem", taskId],
    queryFn: () => fetchData(`/problem/${taskId}`),
    enabled: isEdit,
  });

  // Fetch problem tags if editing
  const { data: problemTags } = useQuery<tagDto[]>({
    queryKey: ["problemTags", taskId],
    queryFn: () => fetchData(`/problem/${taskId}/tags`),
    enabled: isEdit,
  });

  // Initialize form values when editing
  useEffect(() => {
    if (problem) {
      setTitle(problem.title);
      setDifficulty(problem.difficulty);
      if (problem.pdfUrl) setPdfPreview(problem.pdfUrl);
    }
  }, [problem]);

  useEffect(() => {
    if (problemTags) {
      setSelectedTags(problemTags.map((tag) => tag.id));
    }
  }, [problemTags]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0] || null;
    setFile(selectedFile);
    if (selectedFile) {
      setPdfPreview(URL.createObjectURL(selectedFile));
    } else {
      setPdfPreview(null);
    }
  };

  const handleRemovePdf = () => {
    setFile(null);
    setPdfPreview(null);
    if (fileInputRef.current) fileInputRef.current.value = "";
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
        ? await updateFormData<problemDto>(`/problem/${taskId}`, form)
        : await createFormData<problemDto>("/problem", form);

      const problemId = savedProblem.id;

      const oldProblemTags = await fetchData<problemTagDto[]>(
        `/problem-tag?problemId=${problemId}`
      );
      const oldTagIds = oldProblemTags.map((t) => t.tagId);

      // Tags to remove
      const tagsToDelete = oldProblemTags.filter(
        (t) => !selectedTags.includes(t.tagId)
      );

      await Promise.all(
        tagsToDelete.map((t) => deleteData(`/problem-tag/${t.id}`))
      );

      // Tags to add
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
    <div className="bg-[#021526] w-full h-screen text-white">
      <div className="w-1/2 mx-auto p-4 rounded shadow">
        <div className="flex gap-2 py-4">
          <Link to="/tasks">All tasks</Link>
          &gt; {isEdit ? "Edit Task" : "Add Task"}
        </div>
        <form
          onSubmit={handleSubmit}
          className="space-y-4 bg-[#112538] p-4 rounded-2xl border border-[#746F6F]"
        >
          <h2 className="text-xl font-bold mb-4">
            {isEdit ? "Edit Task" : "Create Task"}
          </h2>

          {/* Title */}
          <div>
            <label className="block mb-1 font-medium">Task Title</label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full border rounded px-2 py-1 border-[#746F6F]"
              required
            />
          </div>

          {/* Difficulty */}
          <div>
            <label className="block mb-1 font-medium">Difficulty</label>
            <select
              value={difficulty}
              onChange={(e) => setDifficulty(e.target.value)}
              className="w-full border rounded px-2 py-1 border-[#746F6F]"
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
                  className={`px-3 py-1 rounded border transition-colors duration-200 ${
                    selectedTags.includes(tag.id)
                      ? "bg-green-500 text-white border-green-500 hover:bg-green-600"
                      : "bg-[#1e3a5f] text-white border-[#2a4d77] hover:bg-[#2a4d77]"
                  }`}
                >
                  {tag.name}
                </button>
              ))}
            </div>
          </div>

          {/* PDF Upload */}
          <div className="block mb-1 font-medium">Task Statement (PDF)</div>
          <div className="flex items-center justify-center w-full">
            {pdfPreview ? (
              <div className="relative w-full h-64 border rounded-lg overflow-hidden">
                <iframe
                  src={pdfPreview}
                  title="PDF Preview"
                  width="100%"
                  height="100%"
                ></iframe>
                <button
                  type="button"
                  onClick={handleRemovePdf}
                  className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center hover:bg-red-600"
                >
                  ✕
                </button>
              </div>
            ) : (
              <label
                htmlFor="dropzone-pdf"
                className="flex flex-col items-center justify-center w-full h-64 border-2 border-[#746F6F] border-dashed rounded-lg cursor-pointer bg-[#112538] hover:bg-[#0f2132]"
              >
                <div className="flex flex-col items-center justify-center pt-5 pb-6">
                  <svg
                    className="w-8 h-8 mb-4 text-gray-500"
                    aria-hidden="true"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 20 16"
                  >
                    <path
                      stroke="currentColor"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"
                    />
                  </svg>
                  <p className="mb-2 text-sm text-gray-500">
                    <span className="font-semibold">Click to upload</span> or
                    drag and drop
                  </p>
                  <p className="text-xs text-gray-500">PDF only</p>
                </div>
                <input
                  id="dropzone-pdf"
                  type="file"
                  accept=".pdf"
                  onChange={handleFileChange}
                  className="hidden"
                  ref={fileInputRef}
                />
              </label>
            )}
          </div>

          {/* Submit Buttons */}
          <div className="flex gap-2">
            <button
              type="submit"
              className="w-1/2 bg-green-500 text-white py-2 rounded hover:bg-green-600"
              disabled={saveMutation.isPending}
            >
              {saveMutation.isPending
                ? "Saving..."
                : isEdit
                ? "Update Problem"
                : "Create Problem"}
            </button>
            <Link
              to="/tasks"
              className="w-1/2 bg-red-500 text-white py-2 rounded hover:bg-green-600 text-center"
            >
              Cancel
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
