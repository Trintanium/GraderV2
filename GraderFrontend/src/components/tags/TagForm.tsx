import { useState, useEffect } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchData, createData, updateData } from "@/libs/fetchUtils";
import { tagDto } from "@/types/dto";

interface TagFormProps {
  onClose: () => void;
  tagId: number | null;
}

export default function TagForm({ onClose, tagId }: TagFormProps) {
  const queryClient = useQueryClient();
  const [name, setName] = useState("");

  // Fetch tag if editing
  const { data: tag } = useQuery<tagDto>({
    queryKey: ["tags", tagId],
    queryFn: () => fetchData(`/tag/${tagId}`),
    enabled: !!tagId,
  });

  useEffect(() => {
    if (tag) {
      setName(tag.name);
    }
  }, [tag]);

  // Mutation for create/update
  const mutation = useMutation({
    mutationFn: async () => {
      if (tagId) {
        return updateData(`/tag/${tagId}`, { name });
      } else {
        return createData("/tag", { name });
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tags"] });
      onClose(); // Close modal after save
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate();
  };

  return (
    <div className="max-w-md mx-auto p-4 bg-[#112538] rounded shadow">
      <div
        className="flex justify-between mb-6
      "
      >
        <h2 className="text-xl font-bold text-white">
          {tagId ? "Edit Tag" : "Create Tag"}
        </h2>
        <button onClick={onClose} className=" text-white">
          X
        </button>
      </div>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block mb-1 font-medium text-white">Tag Name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border border-[#17548B] rounded px-2 py-1 text-white"
            required
          />
        </div>

        <div>
          <button
            type="submit"
            className="w-full border border-[#17548B] text-white py-2 rounded bg-[#03346E] hover:bg-[#032249] duration-500"
            disabled={mutation.isPending}
          >
            {mutation.isPending ? "Saving..." : tagId ? "Update" : "Save"}
          </button>
        </div>
      </form>
    </div>
  );
}
