interface SkillBadgeProps {
  skill: string
  confidence?: number
  variant?: "default" | "missing" | "improvement"
}

export function SkillBadge({ skill, confidence, variant = "default" }: SkillBadgeProps) {
  const variants = {
    default: "bg-accent-light text-primary",
    missing: "bg-red-100 text-red-700",
    improvement: "bg-yellow-100 text-yellow-700",
  }

  return (
    <div className={`inline-flex items-center gap-2 px-3 py-2 rounded-full text-sm font-medium ${variants[variant]}`}>
      <span>{skill}</span>
      {confidence && <span className="text-xs font-semibold">{Math.round(confidence * 100)}%</span>}
    </div>
  )
}
