import { Chip } from '@mui/material';
import React from 'react'

const GetStatusChip = ({ status }) => {
    const configs={
        ACTIVE:{label:"Active",colors:"success"},
        OVERDUE:{label:"Overdue",colors:"error"},
        PENDING:{label:"Pending",colors:"warning"},
        READY:{label:"Ready for Pickup",colors:"info"},
    }
    const config=configs[status] || {label : status,colors:"default"};
  return (
        <Chip label={config.label} color={config.colors} />
  )
}

export default GetStatusChip
