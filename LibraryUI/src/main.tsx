import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import LibraryApp from "./App.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <LibraryApp/>
  </StrictMode>,
)

document.getElementById("root").style.height = "100vh";
