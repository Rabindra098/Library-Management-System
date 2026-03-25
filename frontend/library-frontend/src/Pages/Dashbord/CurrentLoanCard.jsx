import { AccessTime } from '@mui/icons-material'
import React from 'react'
import GetStatusChip from './GetStatusChip'
import { Button, Chip } from '@mui/material'

const CurrentLoanCard = ({ loan }) => {
  return (
    <div className='flex items-center justify-between p-6 border border-gray-200 rounded-2xl shadow-sm'>
        <div className='flex items-center space-x-4 flex-1'>
            <div>
                <img src={loan.bookCoverImage} alt={loan.bookTitle} className='w-16 h-20 rounded-lg' />
            </div>
            <div>
                <h4 className='text-lg font-bold text-gray-900 mb-1'>{loan.bookTitle}</h4>
                <p className='text-gray-600'>by {loan.bookAuther}</p>
                <div className='flex items-center space-x-4 text-sm'>
                <div className='flex items-center space-x-4 text-sm'>
                    <AccessTime sx={{ fontSize: 16, color: "#6B7280" }} />
                    <span>Due: {new Date(loan.dueDate).toLocaleDateString()}</span>
                </div>
                <GetStatusChip status={loan.status} />
                        <Chip
                        label={`${loan.remainingDays > 0 ? loan.remainingDays : loan.overdueDays} days ${
                            loan.remainingDays >= 0 ? "remaining" : "overdue"
                        }`}
                        size="small"
                        variant="outlined"
                        />
                </div>
            </div>
        </div>
        <div>
            {/* butten */}
            <Button variant="contained" color="primary">View</Button>
        </div>
    </div>
  )
}

export default CurrentLoanCard
