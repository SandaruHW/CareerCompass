# CareerCompass

An AI-Powered Resume Analyzer + Job Recommendation Platform for job seekers, candidates, and recruiters.

## 📋 Project Overview

**CareerCompass** is a comprehensive SaaS web application designed to help job seekers and recruiters by:

- **CV Analysis**: Parse and extract structured data from uploaded CVs (PDF, DOCX) including skills, experience, and education
- **Job Matching**: Use AI embeddings and similarity search to match candidates with relevant job postings
- **Intelligent Recommendations**: Provide AI-driven suggestions for missing skills, learning resources, CV improvements, and ranked job recommendations
- **Admin Dashboard**: Manage job postings, view analytics, and moderate content

## 🎯 Core Goals & Success Criteria

- **Functional**: Accept CV file → return skills + job matches with scores. Admin can manage jobs.
- **Performance**: Resume analysis complete within 5–10s for normal CVs. Embedding similarity queries under 200ms.
- **Reliability**: Unit tests cover core logic with CI running on pushes and PRs.
- **Deployable**: Dockerized microservices with GitHub Actions CI/CD, ready for cloud deployment.

## 👥 User Roles

- **Guest / Candidate**: Upload CV, view analysis and recommendations, save CVs, sign up
- **Recruiter / Job Seeker**: Save favorite jobs, apply, manage profile
- **Admin**: CRUD job posts, view analytics, moderate content

## 🛠 Tech Stack

### Frontend ✅
- **Framework**: Next.js 16.0.7 (React + TypeScript)
- **Styling**: Tailwind CSS 4.1.17 + shadcn/ui components
- **State Management**: React Hook Form + Zod for validation
- **UI Components**: Radix UI + shadcn/ui library (Accordion, Dialog, Select, etc.)
- **Charting**: Recharts for data visualization
- **Icons**: Lucide React
- **Animation**: Tailwind CSS Animate
- **Build Tool**: Turbopack (integrated with Next.js)

### Backend (Planned)
- **Framework**: Spring Boot (Java)
- **Authentication**: Spring Security with JWT + refresh tokens
- **Database ORM**: Spring Data JPA
- **REST API**: Spring Web
- **Build Tool**: Maven/Gradle

### ML Microservice (Planned)
- **Language**: Python 3.11
- **Framework**: FastAPI
- **NLP**: spaCy, sentence-transformers
- **PDF Processing**: pdfminer, tika-python
- **Document Processing**: python-docx

### Data & Infrastructure
- **Primary Database**: PostgreSQL 15
- **Vector Database**: Milvus / Weaviate / FAISS
- **Cache**: Redis 7
- **File Storage**: S3-compatible (AWS S3 / MinIO)
- **Authentication**: JWT with refresh tokens
- **Containerization**: Docker + docker-compose
- **CI/CD**: GitHub Actions
- **Deployment**: Vercel (frontend), Render/AWS (backend)

## 📁 Project Structure

```
CareerCompass/
├── frontend/                  # React + Next.js frontend
│   ├── app/                   # Next.js app directory
│   │   ├── layout.tsx         # Root layout
│   │   ├── page.tsx           # Landing page
│   │   ├── admin/             # Admin dashboard
│   │   ├── analysis/          # CV analysis results
│   │   ├── dashboard/         # User dashboard
│   │   ├── jobs/              # Job listing & details
│   │   ├── login/             # Login page
│   │   ├── signup/            # Signup page
│   │   └── upload/            # CV upload page
│   ├── components/            # React components
│   │   ├── ui/                # shadcn/ui components
│   │   ├── shared/            # Shared components (Navbar, Sidebar, etc.)
│   │   └── pages/             # Page-specific components
│   ├── hooks/                 # Custom React hooks
│   ├── lib/                   # Utilities & helpers
│   ├── styles/                # Global styles
│   ├── package.json
│   ├── tsconfig.json
│   ├── next.config.mjs
│   ├── postcss.config.mjs
│   └── components.json        # shadcn/ui config
├── backend/                   # Spring Boot backend (Planned)
├── ml-service/                # Python ML microservice (Planned)
├── docker-compose.yml         # Development orchestration
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Node.js 18+ with pnpm
- Docker & Docker Compose (for full stack)
- Python 3.11+ (for ML microservice)
- Java 17+ (for Spring Boot backend)

### Frontend Setup

```bash
cd frontend
pnpm install
pnpm dev
```

The frontend will be available at `http://localhost:3000`

### Full Stack Setup (with Docker)

```bash
docker-compose up --build
```

This starts:
- Frontend: http://localhost:3000
- Backend API: http://localhost:5000
- ML Service: http://localhost:8001
- PostgreSQL: localhost:5432
- Redis: localhost:6379

## 🔄 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (Next.js)                       │
│          React + TypeScript + Tailwind CSS                  │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ↓ (REST API)
┌──────────────────────────────────────────────────────────────┐
│                   Backend API (Spring Boot)                  │
│    Auth, User Mgmt, File Upload, Job CRUD, Orchestration    │
└────┬──────────────────┬──────────────────┬──────────────────┘
     │                  │                  │
     ↓                  ↓                  ↓
┌────────────┐   ┌──────────────┐   ┌──────────────┐
│ PostgreSQL │   │   Redis      │   │ S3 Storage   │
│ (Users,    │   │   (Cache &   │   │ (CV Files)   │
│  Jobs,     │   │   Sessions)  │   │              │
│  Resumes)  │   └──────────────┘   └──────────────┘
└────────────┘
                       ↓ (gRPC/REST)
┌──────────────────────────────────────────────────────────────┐
│            ML Microservice (Python FastAPI)                  │
│  Text Extraction, NLP, Embeddings, Skill Extraction          │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ↓
┌──────────────────────────────────────────────────────────────┐
│          Vector Database (Milvus / Weaviate / FAISS)         │
│           (Embeddings & Similarity Search)                   │
└──────────────────────────────────────────────────────────────┘
```

## 📊 Database Schema (Simplified)

### Core Tables
- **users**: User accounts with roles (guest, recruiter, admin)
- **resumes**: Uploaded CV documents with metadata
- **skills**: Canonical skill catalog with mappings
- **resume_skills**: Links between resumes and skills with confidence scores
- **jobs**: Job postings with descriptions and metadata
- **job_skills**: Links between jobs and required skills with importance weights
- **analysis_results**: CV analysis outputs and recommendations
- **embeddings_meta**: Maps database records to vector store IDs
- **applications**: Job applications from users

## 🧠 ML Pipeline Overview

1. **Text Extraction**: PDF/DOCX → raw text
2. **Preprocessing**: Normalize, clean, tokenize
3. **Named Entity Recognition**: Extract dates, job titles, companies
4. **Skill Extraction**: Rule-based + ML classifier pattern matching
5. **Embeddings**: Generate vector representations using sentence-transformers
6. **Analysis**: Compute similarity scores, identify missing skills
7. **Recommendations**: Suggest jobs, courses, skill improvements

## 🔗 REST API Endpoints (Planned)

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token

### Resume Management
- `POST /api/resumes` - Upload resume (multipart)
- `GET /api/resumes/{id}` - Get resume + analysis
- `GET /api/users/{id}/resumes` - List user's resumes

### Jobs
- `GET /api/jobs` - List jobs (with filters)
- `POST /api/jobs` - Create job (admin only)
- `GET /api/jobs/{id}` - Job details

### Matches & Analysis
- `GET /api/matches/{resume_id}` - Ranked job matches
- `GET /api/analysis/{resume_id}` - Detailed analysis

### Admin
- `GET /api/admin/stats` - Analytics dashboard

## 🧪 Testing Strategy

### Backend Testing
- Unit tests with Jest/pytest
- Integration tests with test containers
- API endpoint testing with Postman/REST Client

### Frontend Testing
- React Testing Library for component tests
- End-to-end tests with Playwright/Cypress
- Visual regression testing

### Coverage Target: 80%+

## 🚢 CI/CD & Deployment

### GitHub Actions Workflows
- **ci.yml**: Run tests, lint, build on every PR
- **cd-deploy.yml**: Auto-deploy on main branch merge

### Deployment Strategy
- **Frontend**: Vercel (auto-deploy from main)
- **Backend API**: Render / AWS EC2 / Railway
- **ML Service**: AWS ECS / Render
- **Databases**: AWS RDS (PostgreSQL), AWS ElastiCache (Redis)

## 📦 Dependencies & Versions

### Frontend Dependencies
```
next: 16.0.7
react: 19.2.0
typescript: 5.9.3
tailwindcss: 4.1.17
recharts: 3.5.1
react-hook-form: 7.68.0
zod: 3.25.76
```

Full dependency list available in `frontend/package.json`

## 🔐 Security Considerations

- JWT authentication with refresh tokens
- HTTPS/TLS for all communications
- CORS configuration
- Input validation with Zod
- SQL injection protection via ORM
- Rate limiting on API endpoints
- Secure file upload validation
- Environment variable management

## 📝 License

[To be determined]

## 👨‍💻 Development

### Available Scripts (Frontend)

```bash
pnpm dev       # Start development server
pnpm build     # Build for production
pnpm start     # Start production server
pnpm lint      # Run ESLint
```

### Environment Variables

Create a `.env.local` file in the frontend directory:

```env
NEXT_PUBLIC_API_URL=http://localhost:5000
NEXT_PUBLIC_ENV=development
```

## 📚 Resources & References

- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS](https://tailwindcss.com)
- [shadcn/ui Components](https://ui.shadcn.com)
- [React Hook Form](https://react-hook-form.com)
- [Zod Validation](https://zod.dev)
- [FastAPI Documentation](https://fastapi.tiangolo.com)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## 🤝 Contributing

1. Create a feature branch (`git checkout -b feature/amazing-feature`)
2. Commit changes (`git commit -m 'Add amazing feature'`)
3. Push to branch (`git push origin feature/amazing-feature`)
4. Open a Pull Request

## 📞 Support

For issues, questions, or suggestions, please open an issue on GitHub.

---

**Status**: 🚧 Under Development - Frontend initialized, Backend & ML Service coming soon
