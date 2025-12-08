"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import Link from "next/link"
import { Zap, BarChart3, Briefcase, ArrowRight } from "lucide-react"

export function LandingPage() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-neutral-50 to-white">
      {/* Navigation */}
      <nav className="sticky top-0 z-50 bg-white border-b border-neutral-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Briefcase className="w-8 h-8 text-primary" />
            <span className="text-2xl font-bold text-primary">CareerCompass</span>
          </div>
          <div className="hidden md:flex items-center gap-8">
            <a href="#features" className="text-neutral-600 hover:text-primary transition">
              Features
            </a>
            <a href="#how" className="text-neutral-600 hover:text-primary transition">
              How It Works
            </a>
            <a href="#testimonials" className="text-neutral-600 hover:text-primary transition">
              Testimonials
            </a>
          </div>
          <div className="flex items-center gap-4">
            <Link href="/login">
              <Button variant="outline">Sign In</Button>
            </Link>
            <Link href="/signup">
              <Button className="bg-primary hover:bg-primary-dark">Get Started</Button>
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
        <h1 className="text-5xl md:text-6xl font-bold text-neutral-900 mb-6 text-balance">
          Smart Career Insights <span className="text-primary">Powered by AI</span>
        </h1>
        <p className="text-xl text-neutral-600 mb-8 max-w-2xl mx-auto text-balance">
          Get AI-powered resume analysis, personalized job recommendations, and actionable career insights in seconds.
        </p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center mb-12">
          <Link href="/upload">
            <Button size="lg" className="bg-primary hover:bg-primary-dark w-full sm:w-auto">
              Analyze Your Resume <ArrowRight className="w-4 h-4 ml-2" />
            </Button>
          </Link>
          <Button size="lg" variant="outline" className="w-full sm:w-auto bg-transparent">
            Watch Demo
          </Button>
        </div>
        <div className="bg-gradient-to-b from-primary/10 to-accent/5 rounded-xl p-12 h-64 flex items-center justify-center border border-primary/20">
          <div className="text-center">
            <BarChart3 className="w-16 h-16 text-primary mx-auto mb-4 opacity-50" />
            <p className="text-neutral-500">Dashboard Preview</p>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <h2 className="text-4xl font-bold text-center mb-16 text-neutral-900">Powerful Features for Career Growth</h2>
        <div className="grid md:grid-cols-3 gap-8">
          {[
            {
              icon: Zap,
              title: "AI Resume Analysis",
              description: "Get instant insights on your resume's strengths and areas for improvement.",
            },
            {
              icon: BarChart3,
              title: "Smart Job Matching",
              description: "Discover jobs that align perfectly with your skills and experience.",
            },
            {
              icon: Briefcase,
              title: "Career Guidance",
              description: "Receive personalized recommendations to advance your career.",
            },
          ].map((feature, idx) => (
            <Card key={idx} className="p-8 hover:shadow-lg transition-shadow">
              <feature.icon className="w-12 h-12 text-primary mb-4" />
              <h3 className="text-xl font-semibold mb-3">{feature.title}</h3>
              <p className="text-neutral-600">{feature.description}</p>
            </Card>
          ))}
        </div>
      </section>

      {/* How It Works */}
      <section id="how" className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 bg-neutral-50/50">
        <h2 className="text-4xl font-bold text-center mb-16 text-neutral-900">How It Works</h2>
        <div className="grid md:grid-cols-3 gap-12">
          {[
            { step: 1, title: "Upload Resume", desc: "Drop your resume and let our AI analyze it." },
            { step: 2, title: "Get Insights", desc: "Receive detailed analysis and improvement tips." },
            { step: 3, title: "Find Jobs", desc: "Discover matched opportunities instantly." },
          ].map((item) => (
            <div key={item.step} className="text-center">
              <div className="w-16 h-16 bg-primary text-white rounded-full flex items-center justify-center mx-auto mb-4 text-2xl font-bold">
                {item.step}
              </div>
              <h3 className="text-xl font-semibold mb-2">{item.title}</h3>
              <p className="text-neutral-600">{item.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Testimonials */}
      <section id="testimonials" className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <h2 className="text-4xl font-bold text-center mb-16 text-neutral-900">What Users Say</h2>
        <div className="grid md:grid-cols-3 gap-8">
          {[
            {
              name: "Sarah Chen",
              role: "Product Manager",
              text: "CareerCompass helped me land my dream role at a top tech company.",
            },
            {
              name: "James Martinez",
              role: "Software Engineer",
              text: "The insights were incredibly accurate and helped me improve my resume.",
            },
            {
              name: "Emily Watson",
              role: "Data Analyst",
              text: "Found perfect job matches within days of analyzing my resume.",
            },
          ].map((testimonial, idx) => (
            <Card key={idx} className="p-8">
              <div className="flex gap-1 mb-4">
                {[...Array(5)].map((_, i) => (
                  <span key={i} className="text-accent">
                    ★
                  </span>
                ))}
              </div>
              <p className="text-neutral-700 mb-4 italic">{testimonial.text}</p>
              <div>
                <p className="font-semibold text-neutral-900">{testimonial.name}</p>
                <p className="text-sm text-neutral-500">{testimonial.role}</p>
              </div>
            </Card>
          ))}
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-neutral-900 text-neutral-100 py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-4 gap-8 mb-8">
            <div>
              <div className="flex items-center gap-2 mb-4">
                <Briefcase className="w-6 h-6 text-accent" />
                <span className="font-bold">CareerCompass</span>
              </div>
              <p className="text-neutral-400">Your AI-powered career guide.</p>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Product</h4>
              <ul className="space-y-2 text-neutral-400">
                <li>
                  <a href="#" className="hover:text-accent transition">
                    Features
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-accent transition">
                    Pricing
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-accent transition">
                    FAQ
                  </a>
                </li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Company</h4>
              <ul className="space-y-2 text-neutral-400">
                <li>
                  <a href="#" className="hover:text-accent transition">
                    About
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-accent transition">
                    Blog
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-accent transition">
                    Contact
                  </a>
                </li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Legal</h4>
              <ul className="space-y-2 text-neutral-400">
                <li>
                  <a href="#" className="hover:text-accent transition">
                    Privacy
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-accent transition">
                    Terms
                  </a>
                </li>
              </ul>
            </div>
          </div>
          <div className="border-t border-neutral-700 pt-8 text-center text-neutral-400">
            <p>&copy; 2025 CareerCompass. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}
