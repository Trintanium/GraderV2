import { useState } from "react";
import { useUser } from "@/contexts/UserContext";
import { deleteData, fetchData } from "@/libs/fetchUtils";
import { tagDto } from "@/types/dto";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit, faTrash, faTag } from "@fortawesome/free-solid-svg-icons";
import TagForm from "@/components/tags/TagForm";

export default function Tags() {
  const { user } = useUser();
  const queryClient = useQueryClient();
  const [isOpen, setIsOpen] = useState(false);
  const [editingTagId, setEditingTagId] = useState<number | null>(null);

  const handleClose = () => {
    setIsOpen(false);
    setEditingTagId(null); // reset after closing
  };

  const handleOpenCreate = () => {
    setEditingTagId(null);
    setIsOpen(true);
  };

  const handleOpenEdit = (id: number) => {
    setEditingTagId(id);
    setIsOpen(true);
  };

  const {
    data: tags = [],
    isLoading: loadingTags,
    error: errorTags,
  } = useQuery<tagDto[]>({
    queryKey: ["tags"],
    queryFn: () => fetchData("/tag"),
  });

  const deleteTagMutation = useMutation({
    mutationFn: (id: number) => deleteData(`/tag/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tags"] });
    },
  });

  if (loadingTags)
    return (
      <div className="flex items-center justify-center h-screen text-gray-500 text-lg">
        Loading...
      </div>
    );
  if (errorTags)
    return (
      <div className="flex items-center justify-center h-screen text-red-500 text-lg">
        Error loading tags: {errorTags.message}
      </div>
    );

  return (
    <div className="flex flex-col w-full min-h-screen  bg-[#021526] gap-6">
      {isOpen && (
        <div className="absolute w-full h-full flex justify-center items-center backdrop-blur-xs">
          <TagForm onClose={handleClose} tagId={editingTagId} />
        </div>
      )}

      {/* Header */}
      <div className="flex flex-col p-8 gap-6">
        <div className="flex flex-col items-start gap-4">
          <h1 className="flex items-center gap-2 text-3xl font-bold text-white">
            <div className=" rounded-full bg-[#6EA4DA] p-2">
              <FontAwesomeIcon icon={faTag} className=" text-white" />
            </div>
            <div className="">Tags Management</div>
          </h1>
          {user && (
            <div
              onClick={handleOpenCreate}
              className="p-4 bg-[#17548B] text-white rounded-lg shadow hover:bg-[#112538] transition duration-200"
            >
              + Add New Tag
            </div>
          )}
        </div>

        {/* Tags List */}
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 w-full">
          {tags.length ? (
            tags.map((tag) => (
              <div
                key={tag.id}
                className="bg-[#112538] rounded-xl shadow-md p-8 flex justify-between items-center hover:shadow-xl transform hover:scale-105 transition"
              >
                <div className="text-lg font-semibold text-white bg-[#47548B] px-6 py-1 rounded-full">
                  {tag.name}
                </div>
                {user && (
                  <div className="flex gap-4  flex-wrap">
                    <button
                      onClick={() => handleOpenEdit(tag.id)}
                      className="flex-1 text-white text-center text-md transition-transform duration-200 hover:scale-110"
                    >
                      <FontAwesomeIcon icon={faEdit} />
                    </button>
                    <button
                      onClick={() => deleteTagMutation.mutate(tag.id)}
                      className="flex-1 text-red-400 text-center text-md transition-transform duration-200 hover:scale-110"
                    >
                      <FontAwesomeIcon icon={faTrash} />
                    </button>
                  </div>
                )}
              </div>
            ))
          ) : (
            <div className="col-span-full p-6 text-center text-gray-700 bg-gray-200 rounded-lg">
              No tags found
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
