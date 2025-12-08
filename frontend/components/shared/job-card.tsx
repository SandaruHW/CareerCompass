import { Card } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { MapPin } from "lucide-react"
import Link from "next/link"

interface JobCardProps {
  id: string
  title: string
  company: string
  location: string
  skills: string[]
  matchScore?: number
}

export function JobCard({ id, title, company, location, skills, matchScore }: JobCardProps) {
  return (
    <Card className="p-6 hover:shadow-lg transition-shadow">
      <div className="flex justify-between items-start mb-4">
        <div>
          <h3 className="text-lg font-semibold text-neutral-900">{title}</h3>
          <p className="text-neutral-600">{company}</p>
        </div>
        {matchScore && (
          <div className="bg-primary text-white px-3 py-1 rounded-full text-sm font-bold">{matchScore}% Match</div>
        )}
      </div>

      <div className="flex items-center gap-1 text-neutral-600 mb-4">
        <MapPin className="w-4 h-4" />
        <span className="text-sm">{location}</span>
      </div>

      <div className="flex flex-wrap gap-2 mb-4">
        {skills.slice(0, 3).map((skill) => (
          <Badge key={skill} variant="secondary">
            {skill}
          </Badge>
        ))}
        {skills.length > 3 && <Badge variant="secondary">+{skills.length - 3}</Badge>}
      </div>

      <Link href={`/jobs/${id}`}>
        <Button className="w-full bg-primary hover:bg-primary-dark">View Details</Button>
      </Link>
    </Card>
  )
}
