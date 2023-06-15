import React, { Component } from 'react';

interface FileAnalysisResult {
  variableCount: number;
  functionCount: number;
  classCount: number;
  interfaceCount: number;
  objectCount: number;
}

interface FileAnalyzerProps {
  // Dodaj odpowiednie propsy
}

interface FileAnalyzerState {
  file: File | null;
  analysisResult: FileAnalysisResult | null;
  error: string | null;
}

class FileAnalyzer extends Component<FileAnalyzerProps, FileAnalyzerState> {
  constructor(props: FileAnalyzerProps) {
    super(props);
    this.state = {
      file: null,
      analysisResult: null,
      error: null,
    };
  }

  handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files && event.target.files[0];
    if (file) {
      this.setState({
        file,
        analysisResult: null,
        error: null,
      });
    }
  };

  handleFileUpload = async () => {
    const { file } = this.state;

    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        const response = await fetch('/api/analyze-file', {
          method: 'POST',
          body: formData,
        });

        if (response.ok) {
          const result: FileAnalysisResult = await response.json();
          this.setState({
            analysisResult: result,
            error: null,
          });
        } else {
          // Obsłuż błędy odpowiedzi
          this.setState({
            analysisResult: null,
            error: 'Wystąpił błąd podczas analizy pliku.',
          });
        }
      } catch (error) {
        // Obsłuż błędy sieciowe
        this.setState({
          analysisResult: null,
          error: 'Wystąpił błąd sieciowy.',
        });
      }
    }
  };

  render() {
    const { analysisResult, error } = this.state;

    return (
      <div className="file-analyzer">
        <div className="file-analyzer__header">
          <label>
  <span>Wybierz plik</span>
  <input type="file" onChange={this.handleFileChange} />
</label>
          <button onClick={this.handleFileUpload}>Wyślij</button>
        </div>

        <div className="file-analyzer__content">
          {analysisResult && (
            <div>
              <h2>Wyniki analizy pliku:</h2>
              <p>Liczba zmiennych: {analysisResult.variableCount}</p>
              <p>Liczba funkcji: {analysisResult.functionCount}</p>
              <p>Liczba klas: {analysisResult.classCount}</p>
              <p>Liczba interfejsów: {analysisResult.interfaceCount}</p>
              <p>Liczba obiektów: {analysisResult.objectCount}</p>
            </div>
          )}

          {error && <p className="file-analyzer__error">Błąd: {error}</p>}
        </div>
		<style>{`
		.file-analyzer {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
  font-family: Arial, sans-serif;
}

.file-analyzer__header {
  margin-bottom: 16px;
}

.file-analyzer__content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.file-analyzer__error {
  color: red;
}

.file-analyzer__header input[type="file"] {
  display: none;
}

.file-analyzer__header label {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.file-analyzer__header label:hover {
  background-color: #45a049;
}

.file-analyzer__header label input[type="file"] {
  display: none;
}

.file-analyzer__header label span {
  padding: 0 8px;
}


		`}</style>
      </div>  
    );
  }
}

export default FileAnalyzer;
