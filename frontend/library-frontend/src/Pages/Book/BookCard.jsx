import { Person } from "@mui/icons-material";
import { Button } from "@mui/material";
import React from "react";

const BookCard = ({ book }) => {
  const handleViewDetails = () => {
    console.log(`Viewing details for book ID: ${book.id}`);
  };

  return (
    <div className="group bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 overflow-hidden cursor-pointer border border-gray-100 hover:-translate-y-1">
      <div className="relative h-64 bg-gradient-to-br from-indigo-100 to-purple-100 overflow-hidden">
        <img
          // eslint-disable-next-line no-constant-binary-expression
          src={"https://cdn.pixabay.com/photo/2019/01/30/08/30/book-3964050_1280.jpg" ||
            book.coverImageUrl
          }
          alt={book.title}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
        />
      </div>

      <div className="p-5">
        <h3 className="text-lg font-bold text-gray-900 mb-2 line-clamp-2 group-hover:text-indigo-600 transition-colors">
          {book.title}
        </h3>

        <div className="flex items-center space-x-2 text-gray-600 mb-3">
          <Person sx={{ fontSize: 16 }} />
          <span className="text-sm line-clamp-1">{book.author}</span>
        </div>

        <div className="flex items-center justify-between text-xs text-gray-500 mb-4">
          <span>ISBN: {book.isbn}</span>
          <span>
            {book.availableCopies}/{book.totalCopies} copies
          </span>
        </div>

        {book.description && (
          <p className="text-sm text-gray-600 mb-4 line-clamp-2">
            {book.description}
          </p>
        )}

        <div className="flex gap-2 mt-auto pt-4">
          <Button
            fullWidth
            onClick={handleViewDetails}
            sx={{
              textTransform: "none",
              fontWeight: 600,
              fontSize: "0.95rem",
              py: 1.2,
              borderRadius: "10px",
              color: "#4F46E5",
              border: "1.5px solid #4F46E5",
              backgroundColor: "#EEF2FF",
              transition: "all 0.25s ease",
              "&:hover": {
                backgroundColor: "#4F46E5",
                color: "#FFFFFF",
                boxShadow: "0 6px 18px rgba(79,70,229,0.25)",
                transform: "translateY(-1px)",
              },
            }}
          >
            View
          </Button>
        </div>
      </div>
    </div>
  );
};

export default BookCard;
