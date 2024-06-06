import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)


 

   fetch('https://tu-app-en-railway.up.railway.app/productos')
   .then(response => {
     if (!response.ok) {
       throw new Error('Network response was not ok ' + response.statusText);
     }
     return response.json();
   })
   .then(data => {
     console.log(data); // Manejar los datos obtenidos
   })
   .catch(error => {
     console.error('Hubo un problema con la solicitud fetch:', error);
   });

  return (
    <>
      <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App
