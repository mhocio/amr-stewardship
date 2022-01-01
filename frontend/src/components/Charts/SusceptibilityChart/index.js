import React, { useCallback } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import Grid from '@mui/material/Grid'
import DownloadChartButton from '../../DownloadChartButton';
import FileSaver from "file-saver";
import { useCurrentPng } from "recharts-to-png";
import { Paper } from '@mui/material';

const data = [
  {
    name: 'Page A',
    uv: 4000,
    pv: 2400,
    amt: 2400,
  },
  {
    name: 'Page B',
    uv: 3000,
    pv: 1398,
    amt: 2210,
  },
  {
    name: 'Page C',
    uv: 2000,
    pv: 9800,
    amt: 2290,
  },
  {
    name: 'Page D',
    uv: 2780,
    pv: 3908,
    amt: 2000,
  },
  {
    name: 'Page E',
    uv: 1890,
    pv: 4800,
    amt: 2181,
  },
  {
    name: 'Page F',
    uv: 2390,
    pv: 3800,
    amt: 2500,
  },
  {
    name: 'Page G',
    uv: 3490,
    pv: 4300,
    amt: 2100,
  },
];

export default function SusceptibilityChart() {

  const [getPng, { ref: myRef }] = useCurrentPng();
  const handleDownload = useCallback(async () => {
    const png = await getPng();
    if (png) {
      FileSaver.saveAs(png, "wykres.png");
    }
  }, [getPng]);

  return (
    <Grid container direction="column" spacing={3} wrap="nowrap">
      <Grid item xs={9}>
        <Paper sx={{ padding: '20px' }}>
          <ResponsiveContainer width="99%" aspect={1} maxHeight={500}>
            <BarChart
              ref={myRef}
              width={500}
              height={300}
              data={data}
              margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="pv" fill="#8884d8" />
              <Bar dataKey="uv" fill="#82ca9d" />
            </BarChart>
          </ResponsiveContainer>
        </Paper>
      </Grid>
      <Grid item>
        <DownloadChartButton handle={handleDownload} />
      </Grid>
    </Grid>
  );
}
