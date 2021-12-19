import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';

const columns = [
  { field: 'id', headerName: 'ID', width: 100 },
  { field: 'firstName', headerName: 'Imię', width: 130 },
  { field: 'lastName', headerName: 'Nazwisko', width: 130 }
];

const rows = [
  { id: 1, lastName: 'Snow', firstName: 'Jon'},
  { id: 2, lastName: 'Lannister', firstName: 'Cersei'},
  { id: 3, lastName: 'Lannister', firstName: 'Jaime'},
  { id: 4, lastName: 'Stark', firstName: 'Arya'},
  { id: 5, lastName: 'Targaryen', firstName: 'Daenerys'},
  { id: 6, lastName: 'Melisandre', firstName: 'Jarre',},
  { id: 7, lastName: 'Clifford', firstName: 'Ferrara'},
  { id: 8, lastName: 'Frances', firstName: 'Rossini'},
  { id: 9, lastName: 'Roxie', firstName: 'Harvey'},
];

export default function DataTable() {
  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        rowsPerPageOptions={[5]}
        checkboxSelection
      />
    </div>
  );
}
