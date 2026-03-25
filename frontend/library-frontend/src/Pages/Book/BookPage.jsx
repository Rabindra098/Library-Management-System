import React from "react";
import GenreFilter from "./GenreFilter";
import {
  FormControl,
  InputAdornment,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import SortIcon from "@mui/icons-material/Sort";
import BookCard from "./BookCard";

const genres = [
  {
    active: true,
    bookCount: 12,
    code: "FICTION",
    createdAt: "2025-10-10T10:40:08.725525",
    description:
      "Genre that includes imaginative or invented stories, often exploring characters and events that are not real.",
    displayOrder: 1,
    id: 1,
    name: "Fiction",
    parentGenreId: null,
    parentGenreName: null,
    subGenres: null,
    updatedAt: "2025-10-10T10:40:08.725525",
  },
  {
    active: true,
    bookCount: 8,
    code: "NON_FICTION",
    createdAt: "2025-10-10T10:41:08.725525",
    description:
      "Genre based on real facts, events, and people, including biographies and documentaries.",
    displayOrder: 2,
    id: 2,
    name: "Non-Fiction",
    parentGenreId: null,
    parentGenreName: null,
    subGenres: null,
    updatedAt: "2025-10-10T10:41:08.725525",
  },
  {
    active: true,
    bookCount: 5,
    code: "SCI_FI",
    createdAt: "2025-10-10T10:42:08.725525",
    description:
      "Genre that explores futuristic science, technology, space travel, and advanced civilizations.",
    displayOrder: 3,
    id: 3,
    name: "Science Fiction",
    parentGenreId: null,
    parentGenreName: null,
    subGenres: null,
    updatedAt: "2025-10-10T10:42:08.725525",
  },
  {
    active: true,
    bookCount: 7,
    code: "FANTASY",
    createdAt: "2025-10-10T10:43:08.725525",
    description:
      "Genre that features magical elements, mythical creatures, and imaginary worlds.",
    displayOrder: 4,
    id: 4,
    name: "Fantasy",
    parentGenreId: null,
    parentGenreName: null,
    subGenres: null,
    updatedAt: "2025-10-10T10:43:08.725525",
  },
  {
    active: true,
    bookCount: 6,
    code: "MYSTERY",
    createdAt: "2025-10-10T10:44:08.725525",
    description:
      "Genre focused on solving crimes, uncovering secrets, and suspenseful investigations.",
    displayOrder: 5,
    id: 5,
    name: "Mystery",
    parentGenreId: null,
    parentGenreName: null,
    subGenres: null,
    updatedAt: "2025-10-10T10:44:08.725525",
  },
];
const books = [
  {
    active: true,
    alreadyHaveLoan: null,
    alreadyHaveReservation: null,
    author: "Ashok Zarmariya",
    availableCopies: 0,
    coverImageUrl: "https://example.com/images/spring-boot.jpg",
    createdAt: "2025-10-25T10:37:00.572600",
    description:
      "An advanced developer's handbook on building scalable, production-grade microservices.",
    genreCode: "PROGRAMMING",
    genreId: 10,
    genreName: "PROGRAMMING",
    id: 1,
    isbn: "978-1-4028-9462-6",
    language: "English",
    pages: 320,
    price: 499,
    publicationDate: "2024-06-25",
    publisher: "Zosh Publications",
    title: "Spring Boot",
    totalCopies: 2,
    updatedAt: "2025-12-25T18:58:46.903410",
  },
  {
    active: true,
    alreadyHaveLoan: null,
    alreadyHaveReservation: null,
    author: "Joshua Bloch",
    availableCopies: 3,
    coverImageUrl: "https://example.com/images/effective-java.jpg",
    createdAt: "2025-09-10T09:20:11.123000",
    description:
      "Best practices for writing robust, maintainable, and efficient Java code.",
    genreCode: "PROGRAMMING",
    genreId: 10,
    genreName: "PROGRAMMING",
    id: 2,
    isbn: "978-0-1323-4687-9",
    language: "English",
    pages: 416,
    price: 650,
    publicationDate: "2023-11-15",
    publisher: "Addison-Wesley",
    title: "Effective Java",
    totalCopies: 5,
    updatedAt: "2025-12-20T12:30:10.450000",
  },
  {
    active: true,
    alreadyHaveLoan: null,
    alreadyHaveReservation: null,
    author: "Robert C. Martin",
    availableCopies: 1,
    coverImageUrl: "https://example.com/images/clean-code.jpg",
    createdAt: "2025-08-05T14:45:30.000000",
    description:
      "A guide to producing readable, reusable, and refactor-friendly code.",
    genreCode: "PROGRAMMING",
    genreId: 10,
    genreName: "PROGRAMMING",
    id: 3,
    isbn: "978-0-1323-5088-3",
    language: "English",
    pages: 464,
    price: 599,
    publicationDate: "2022-08-01",
    publisher: "Prentice Hall",
    title: "Clean Code",
    totalCopies: 3,
    updatedAt: "2025-12-10T09:10:55.220000",
  },
  {
    active: true,
    alreadyHaveLoan: null,
    alreadyHaveReservation: null,
    author: "Kathy Sierra",
    availableCopies: 2,
    coverImageUrl: "https://example.com/images/head-first-java.jpg",
    createdAt: "2025-07-18T11:00:00.000000",
    description:
      "A brain-friendly guide to learning Java with visuals and real-world examples.",
    genreCode: "PROGRAMMING",
    genreId: 10,
    genreName: "PROGRAMMING",
    id: 4,
    isbn: "978-0-5965-0092-6",
    language: "English",
    pages: 720,
    price: 750,
    publicationDate: "2021-05-10",
    publisher: "O'Reilly Media",
    title: "Head First Java",
    totalCopies: 4,
    updatedAt: "2025-12-01T16:40:00.000000",
  },
  {
    active: true,
    alreadyHaveLoan: null,
    alreadyHaveReservation: null,
    author: "Martin Fowler",
    availableCopies: 0,
    coverImageUrl: "https://example.com/images/refactoring.jpg",
    createdAt: "2025-06-01T08:15:45.000000",
    description:
      "Techniques and patterns for improving the design of existing code.",
    genreCode: "PROGRAMMING",
    genreId: 10,
    genreName: "PROGRAMMING",
    id: 5,
    isbn: "978-0-2014-8576-8",
    language: "English",
    pages: 448,
    price: 699,
    publicationDate: "2020-09-20",
    publisher: "Addison-Wesley",
    title: "Refactoring",
    totalCopies: 2,
    updatedAt: "2025-11-15T10:05:30.000000",
  },
];

const BookPage = () => {
  const [selectedGenreId, setSelectedGenreId] = React.useState(null);
  const handleGenreChange = (event) => {
    const genreId = event.target.value;
    setSelectedGenreId(genreId);
  };
  console.log("Selected Genre ID:", selectedGenreId);
  const [availabilityFilter, setAvailabilityFilter] = React.useState("ALL");
  const [searchTerm, setSearchTerm] = React.useState("");
  const [sortBy, setSortBy] = React.useState("createdAt");
  const [sortDirection, setSortDirection] = React.useState("DESC");
  const handleSortChange = (value) => {
    const [field, direction] = value.split("-");
    setSortBy(field);
    setSortDirection(direction.toUpperCase());
  };
  const getCurrentSortValue = () => {
    return `${sortBy}-${sortDirection.toLowerCase()}`;
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-50">
      {/* Header */}
      <div className="bg-white border-b border-gray-200 text-center">
        <div className="px-3 sm:px-5 lg:px-8 py-8">
          <div className="text-4xl text-gray-900 mb-2">
            <h1 className="font-bold">
              Browse Our{" "}
              <span className="bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                Collection
              </span>
            </h1>
            <p className="text-lg text-gray-600 font-semibold">
              Discover Thousands of Books Across Various Genres
            </p>
          </div>
        </div>
      </div>
      {/* main content */}
      <div className="px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex gap-6">
          {/* Side bar */}
          <aside className="lg:w-50 space-y-6 shrink-0">
            <div className="space-y-6">
              <GenreFilter onGenreSelect={handleGenreChange} genres={genres} />

              <div className="bg-white rounded-xl shadow-md p-4 border border-gray-100">
                <h3 className="text-lg font-bold text-gray-900 mb-4 pb-3 border-b border-gray-200">
                  Availability
                </h3>
                <FormControl fullWidth>
                  <Select
                    value={availabilityFilter}
                    onChange={(e) => setAvailabilityFilter(e.target.value)}
                  >
                    <MenuItem value="ALL">All Books</MenuItem>
                    <MenuItem value="AVAILABLE">Available Only</MenuItem>
                    <MenuItem value="CHECKED_OUT">Checked Out</MenuItem>
                  </Select>
                </FormControl>
              </div>
            </div>
          </aside>

          {/* Main Content area */}
          <main className="flex-1 space-y-6">
            {/* Search and Sort */}
            <div className="flex flex-col md:flex-row gap-4">
              {/* Search Input */}
              <div className="flex-1">
                <TextField
                  fullWidth
                  placeholder="Search by title, author, or category..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <SearchIcon className="text-gray-400" />
                      </InputAdornment>
                    ),
                  }}
                />
              </div>

              {/* Sort Dropdown */}
              <div className="md:w-64">
                <FormControl fullWidth>
                  <InputLabel>Sort By</InputLabel>
                  <Select
                    value={getCurrentSortValue()}
                    onChange={(e) => handleSortChange(e.target.value)}
                    label="Sort By"
                    startAdornment={
                      <InputAdornment position="start">
                        <SortIcon className="text-gray-400" />
                      </InputAdornment>
                    }
                  >
                    <MenuItem value="title-asc">Title (A-Z)</MenuItem>
                    <MenuItem value="title-desc">Title (Z-A)</MenuItem>
                    <MenuItem value="author-asc">Author (A-Z)</MenuItem>
                    <MenuItem value="author-desc">Author (Z-A)</MenuItem>
                    <MenuItem value="createdAt-desc">Newest First</MenuItem>
                    <MenuItem value="createdAt-asc">Oldest First</MenuItem>
                  </Select>
                </FormControl>
              </div>
            </div>
            {/* Books grid */}
            <div className="max-w-7xl mx-auto px-6 py-4">
              <div className="max-h-[calc(100vh-260px)] overflow-y-auto pr-2 custom-scrollbar">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                  {books.map((book) => (
                    <BookCard key={book.id} book={book} />
                  ))}
                </div>
              </div>
            </div>
          </main>
        </div>
      </div>
    </div>
  );
};

export default BookPage;
