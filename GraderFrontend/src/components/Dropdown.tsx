interface DropdownProps {
  list: string[];
  label?: string;
  onChange?: (value: string) => void;
}

export default function Dropdown({ list, label, onChange }: DropdownProps) {
  return (
    <div className="flex flex-col gap-2">
      {label && <label className="font-medium">{label}</label>}
      <select
        className="border rounded p-2"
        onChange={(e) => onChange?.(e.target.value)}
      >
        <option value="">Select an option</option>
        {list.map((item, index) => (
          <option key={index} value={item}>
            {item}
          </option>
        ))}
      </select>
    </div>
  );
}
