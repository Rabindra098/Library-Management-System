import React from 'react'
import CurrentLoanCard from './CurrentLoanCard'


const loan={
    bookTitle:"The Great Gatsby",
    bookCoverImage:"https://m.media-amazon.com/images/I/81af+MCATTL.jpg",
    bookAuther:"F. Scott Fitzgerald",
    dueDate:"2024-07-15",
    status:"ACTIVATE",
    remainingDays:10,
    overdueDays:0,
}

const CurrentLoans = () => {
  return (
    <div className='p-6'>
      <h3 className='text-2xl font-bold text-gray-900 mb-6'>
        Book Your're Currently Reading
      </h3>
      <div className='space-y-4'>
        {/* list of current loans */}
        {[1,1,1,1].map((index)=><CurrentLoanCard loan={loan} key={index}/>)}     
      </div>
    </div>
  )
}

export default CurrentLoans

