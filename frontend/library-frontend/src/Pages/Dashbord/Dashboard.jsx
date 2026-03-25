import React from 'react'
import StateCard from './StateCard'
import { AutoAwesome } from '@mui/icons-material'
import StatesConfig from './StatesConfig'
import { Box, LinearProgress, Tab, Tabs } from '@mui/material'
import CurrentLoans from './CurrentLoans'
import Reservation from './Reservation'
import ReadingHistory from './ReadingHistory'
import Recommandation from './Recommandation'


const Dashboard = () => {

    const [activeTab, setActiveTab] = React.useState(0);

    const handleTabChange = (event, newValue) => {
        setActiveTab(newValue);
    }

    const stateData = StatesConfig({        
        myLoans:[1,2,3],
        reservations:[1,2],
        stats:{readingStreak:5}})

  return (
    <div className='min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-500 py-8'>
      <div className='px-4 sm:px-6 lg:px-8'>
            {/* Dashboard Header */}
            <div className='mb-8 animate-fade-in-up'>
                <h1 className='text-4xl font-bold text-indigo-500 mb-2'>
                    My {" "} <span className='bg-gradient-to-r from-indigo-600 to-purple-500 bg-clip-text text-transparent'>Dashboard</span>
                </h1>
                <p className='text-lg text-gray-600'>Track your reading journey and manage your library</p>
            </div>  

            {/* State Cards */}
            <div className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8'>
                {stateData.map((item) =>
                <StateCard 
                    bgColor={item.bgColor}
                    textColor={item.textColor}
                    icon={item.icon}
                    value={item.value}
                    title={item.title}
                    subtitle={item.subtitle}
                    key={item.id} 
                />)}
            </div>

            {/* Reading Progress */}
            <div className='bg-white rounded-2xl shadow-2xl p-6 mb-8'>
                <div className='flex items-center justify-between mb-4'>
                    <div>
                        <h1 className='text-xl font-bold text-gray-900 mb-1'>2026 Reading Goal</h1>
                        <p className='text-gray-600'>
                            {250} of 30 books read
                        </p>
                    </div>
                    <div className='p-3 bg-gradient-to-br from-indigo-100 to-purple-100 rounded-full rounded-full'>
                        <AutoAwesome sx={{ fontSize: 32, color: "#4F46E5" }} />
                    </div>
                </div> 
                    <LinearProgress variant="determinate" value={83} 
                        sx={{ 
                            height: 12,
                            borderRadius: 5,
                            backgroundColor: '#E0E0E0',
                            '& .MuiLinearProgress-bar': {
                                borderRadius: 6,
                                background: 'linear-gradient(to right, #4F46E5, #9333EA)',
                            },
                        }}
                    />
                    <p className='text-sm text-gray-600 mt-2'>83% complete</p>
            </div>

            {/* Tab section */}
            <div className='bg-white shadow-2xl overflow-hidden'>
                <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                    <Tabs 
                    sx={{
                        "& .MuiTab-root":{
                            textTransform:"none",
                            fontSize:"1rem",
                            fontWeight:600
                        },
                        "& .Mui-Selected":{
                            color:"#4F46E5"
                        },
                        "& .MuiTabs-indicator":{
                            backgroundColor:"#4F46E5"
                        }
                    }}
                    value={activeTab} onChange={handleTabChange} aria-label="basic tabs example">
                        <Tab label="Current Loan" />
                        <Tab label="Reservation" />
                        <Tab label="Reading History" />
                        <Tab label="Recommendations" />
                    </Tabs>
                </Box>
                {/* Current Loan Tab */}
                {activeTab === 0 && <CurrentLoans />}
                 {/* Reservation Tab */}
                {activeTab === 1 && <Reservation />}
                 {/* Reading History Tab */}
                {activeTab === 2 && <ReadingHistory />}
                 {/* Recommendations Tab */}
                {activeTab === 3 && <Recommandation />}
            </div>
      </div>
    </div>
  )
}
export default Dashboard
