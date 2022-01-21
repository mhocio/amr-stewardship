import React, { useCallback, useState, useEffect } from 'react';

// material-ui
import { Paper, Grid } from '@mui/material';

// third-party
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { useCurrentPng } from "recharts-to-png";
import FileSaver from "file-saver";

// project imports
import DownloadChartButton from '../../DownloadChartButton';


export default function SusceptibilityChart({ chartData }) {

  const [getPng, { ref: myRef }] = useCurrentPng();
  const [modifiedData, setModifiedData] = useState([]);
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
              data={chartData}
              margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 50,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="Nazwa" interval={0} tick={{ angle: 40, textAnchor: 'start', 'dominantBaseline': 'ideographic' }} />
              <YAxis allowDataOverflow={true} domain={[0, 100]} />
              <Tooltip />
              <Legend layout="horizontal" verticalAlign="top" align="center" />
              {
                chartData[0] ? Object.keys(chartData[0]).map((key, index, arr) => (
                  index !== arr.length - 1 ? <Bar dataKey={key} fill={"#000000".replace(/0/g, function () { return (~~(Math.random() * 16)).toString(16) })} /> : null)) : null
              }
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
