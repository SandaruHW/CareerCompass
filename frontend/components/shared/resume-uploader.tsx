"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { Upload, FileText, X } from "lucide-react"

interface FileUploadResponse {
  id: number
  fileName: string
  originalFileName: string
  contentType: string
  fileSize: number
  status: string
  uploadedAt: string
  message: string
}

export default function ResumeUploader() {
  const [file, setFile] = useState<File | null>(null)
  const [uploading, setUploading] = useState(false)
  const [dragOver, setDragOver] = useState(false)
  const { toast } = useToast()

  const allowedTypes = ['.pdf', '.doc', '.docx']
  const maxFileSize = 10 * 1024 * 1024 // 10MB

  const validateFile = (selectedFile: File) => {
    // Check file type
    const fileExtension = selectedFile.name.toLowerCase().substring(selectedFile.name.lastIndexOf('.'))
    if (!allowedTypes.includes(fileExtension)) {
      throw new Error('Invalid file type. Please upload PDF, DOC, or DOCX files only.')
    }

    // Check file size
    if (selectedFile.size > maxFileSize) {
      throw new Error('File size exceeds 10MB limit.')
    }

    // Check if file name is safe
    if (selectedFile.name.includes('..') || selectedFile.name.includes('/') || selectedFile.name.includes('\\')) {
      throw new Error('Invalid file name. Please rename your file.')
    }
  }

  const handleFileSelect = (selectedFile: File) => {
    try {
      validateFile(selectedFile)
      setFile(selectedFile)
      toast({
        title: "File selected",
        description: `${selectedFile.name} is ready to upload`,
      })
    } catch (error: any) {
      toast({
        title: "Invalid file",
        description: error.message,
        variant: "destructive",
      })
    }
  }

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0]
    if (selectedFile) {
      handleFileSelect(selectedFile)
    }
  }

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault()
    setDragOver(true)
  }

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault()
    setDragOver(false)
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    setDragOver(false)
    
    const droppedFile = e.dataTransfer.files[0]
    if (droppedFile) {
      handleFileSelect(droppedFile)
    }
  }

  const uploadFile = async () => {
    if (!file) return

    setUploading(true)
    
    try {
      const formData = new FormData()
      formData.append('file', file)

      // Get auth token
      const token = localStorage.getItem('careercompass_token')
      if (!token) {
        throw new Error('Please log in to upload files')
      }

      const response = await fetch('http://localhost:8080/api/resumes', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
        body: formData,
      })

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({
          message: `HTTP ${response.status}: ${response.statusText}`,
        }))
        throw new Error(errorData.message || 'Upload failed')
      }

      const result: FileUploadResponse = await response.json()
      
      toast({
        title: "Upload successful",
        description: `${file.name} has been uploaded successfully`,
      })
      
      // Reset form
      setFile(null)
      
    } catch (error: any) {
      console.error('Upload failed:', error)
      toast({
        title: "Upload failed",
        description: error.message || 'Failed to upload file',
        variant: "destructive",
      })
    } finally {
      setUploading(false)
    }
  }

  const removeFile = () => {
    setFile(null)
  }

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  }

  return (
    <Card className="p-6">
      <div className="space-y-4">
        <div>
          <Label className="text-lg font-semibold">Upload Resume</Label>
          <p className="text-sm text-muted-foreground mt-1">
            Upload your resume in PDF, DOC, or DOCX format (max 10MB)
          </p>
        </div>

        {/* File Upload Area */}
        {!file && (
          <div
            className={`
              border-2 border-dashed rounded-lg p-8 text-center transition-colors
              ${dragOver 
                ? 'border-primary bg-primary/5' 
                : 'border-gray-300 hover:border-primary hover:bg-primary/5'
              }
              cursor-pointer
            `}
            onDragOver={handleDragOver}
            onDragLeave={handleDragLeave}
            onDrop={handleDrop}
            onClick={() => document.getElementById('file-upload')?.click()}
          >
            <Upload className="mx-auto h-12 w-12 text-gray-400" />
            <p className="mt-2 text-sm text-gray-600">
              <span className="font-medium text-primary">Click to upload</span> or drag and drop
            </p>
            <p className="text-xs text-gray-500 mt-1">
              PDF, DOC, DOCX up to 10MB
            </p>
            
            <Input
              id="file-upload"
              type="file"
              accept=".pdf,.doc,.docx"
              onChange={handleFileChange}
              className="hidden"
            />
          </div>
        )}

        {/* Selected File Preview */}
        {file && (
          <div className="border rounded-lg p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <FileText className="h-8 w-8 text-primary" />
                <div>
                  <p className="font-medium">{file.name}</p>
                  <p className="text-sm text-gray-500">{formatFileSize(file.size)}</p>
                </div>
              </div>
              <Button
                variant="ghost"
                size="sm"
                onClick={removeFile}
                disabled={uploading}
              >
                <X className="h-4 w-4" />
              </Button>
            </div>
          </div>
        )}

        {/* Upload Button */}
        {file && (
          <Button 
            onClick={uploadFile} 
            disabled={uploading}
            className="w-full"
          >
            {uploading ? 'Uploading...' : 'Upload Resume'}
          </Button>
        )}
      </div>
    </Card>
  )
}