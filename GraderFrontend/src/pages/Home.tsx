function Home() {
  return (
    <div className="w-full min-h-screen bg-[#021526] text-white flex flex-col">
      {/* Hero Section */}
      <div className="h-screen flex flex-col justify-center items-center text-center px-4 bg-gradient-to-b from-[#021526] to-[#112538]">
        <h1 className="text-5xl font-bold mb-4">Grader</h1>
        <p className="text-xl mb-6 max-w-xl">
          Improve your coding skills with real programming problems, automatic
          test case evaluation, and instant feedback.
        </p>
        <a
          href="/tasks"
          className="px-6 py-3 bg-[#0A3160] hover:bg-[#17548B] rounded-lg text-lg font-semibold transition"
        >
          Start Solving
        </a>
      </div>

      {/* Features Section */}
      <div className="py-20 bg-[#112538] px-4">
        <h2 className="text-3xl font-bold text-center mb-12">
          What You Will Learn
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto text-center">
          <div className="p-6 bg-[#0A3160] rounded-xl shadow hover:shadow-lg transition">
            <h3 className="text-xl font-semibold mb-2">Diverse Problems</h3>
            <p>
              Problems ranging from beginner to advanced levels, covering
              multiple topics.
            </p>
          </div>
          <div className="p-6 bg-[#0A3160] rounded-xl shadow hover:shadow-lg transition">
            <h3 className="text-xl font-semibold mb-2">Automatic Evaluation</h3>
            <p>Instant feedback with test cases and precise scoring.</p>
          </div>
          <div className="p-6 bg-[#0A3160] rounded-xl shadow hover:shadow-lg transition">
            <h3 className="text-xl font-semibold mb-2">Track Your Progress</h3>
            <p>Monitor your scores and improvements over time.</p>
          </div>
        </div>
      </div>

      {/* Categories / Popular Problems Section */}
      <div className="py-20 bg-[#0A3160] px-4">
        <h2 className="text-3xl font-bold text-center mb-12">
          Problem Categories
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {["Beginner", "Intermediate", "Advanced"].map((cat) => (
            <div
              key={cat}
              className="p-6 bg-[#112538] rounded-xl shadow hover:shadow-lg transition text-center"
            >
              <h3 className="text-xl font-semibold mb-2">{cat}</h3>
              <p>Practice {cat} level problems</p>
            </div>
          ))}
        </div>
      </div>

      {/* Footer */}
      <footer className="py-6 text-center bg-[#021526]">
        <p>© 2025 Grader. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default Home;
