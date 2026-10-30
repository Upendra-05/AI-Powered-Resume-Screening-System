const API = "http://localhost:8080/api";

async function apiRequest(url, options = {}) {
    const response = await fetch(url, options);

    const text = await response.text();

    let data = null;

    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }
    }

    if (!response.ok) {
        const message =
            data && typeof data === "object"
                ? data.error || data.message || "Request failed"
                : data || "Request failed";

        throw new Error(message);
    }

    return data;
}

function showMessage(elementId, message, isError = false) {
    const element = document.getElementById(elementId);

    element.textContent = message;
    element.className = isError
        ? "message error"
        : "message success";
}

function createElement(tag, text, className = "") {
    const element = document.createElement(tag);

    element.textContent = text;

    if (className) {
        element.className = className;
    }

    return element;
}

function createJobCard(job, includeScore = false) {
    const card = createElement("div", "", "result-card");

    card.appendChild(createElement("h3", job.title || "Untitled Job"));

    card.appendChild(
        createElement(
            "p",
            "Company: " + (job.companyName || "Not specified")
        )
    );

    card.appendChild(
        createElement(
            "p",
            "Location: " + (job.location || "Not specified")
        )
    );

    card.appendChild(
        createElement(
            "p",
            "Required Skills: " + (job.requiredSkills || "Not specified")
        )
    );

    card.appendChild(
        createElement(
            "p",
            "Salary: ₹" + Number(job.salary || 0).toLocaleString("en-IN")
        )
    );

    if (includeScore) {
        card.appendChild(
            createElement(
                "span",
                "Match Score: " + Number(job.matchScore || 0) + "%",
                "match-score"
            )
        );
    }

    card.appendChild(
        createElement(
            "p",
            "Job ID: " + job.jobId
                ? "Job ID: " + (job.jobId ?? job.id)
                : "Job ID: " + job.id
        )
    );

    return card;
}

// User registration
document.getElementById("registerForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const user = {
            name: document.getElementById("name").value.trim(),
            email: document.getElementById("email").value.trim(),
            password: document.getElementById("password").value
        };

        try {
            const result = await apiRequest(API + "/users", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });

            showMessage(
                "registerMessage",
                "Registration successful! Your User ID is " + result.id
            );

            document.getElementById("resumeUserId").value = result.id;
            document.getElementById("recommendationUserId").value = result.id;
            document.getElementById("applicationUserId").value = result.id;
            document.getElementById("viewApplicationsUserId").value = result.id;

            event.target.reset();

        } catch (error) {
            showMessage("registerMessage", error.message, true);
        }
    }
);

// Resume upload
document.getElementById("resumeForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const userId = document.getElementById("resumeUserId").value;
        const file = document.getElementById("resumeFile").files[0];

        if (!file) {
            showMessage("resumeMessage", "Please select a PDF file.", true);
            return;
        }

        const formData = new FormData();

        formData.append("userId", userId);
        formData.append("file", file);

        try {
            await apiRequest(API + "/resumes/upload?userId=" +
                encodeURIComponent(userId), {
                method: "POST",
                body: formData
            });

            showMessage(
                "resumeMessage",
                "Resume uploaded and processed successfully."
            );

            event.target.reset();

        } catch (error) {
            showMessage("resumeMessage", error.message, true);
        }
    }
);

// Create a job
document.getElementById("jobForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const job = {
            title: document.getElementById("jobTitle").value.trim(),
            companyName: document.getElementById("companyName").value.trim(),
            location: document.getElementById("location").value.trim(),
            requiredSkills: document.getElementById("requiredSkills").value.trim(),
            description: document.getElementById("description").value.trim(),
            salary: Number(document.getElementById("salary").value)
        };

        try {
            await apiRequest(API + "/jobs", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(job)
            });

            showMessage("jobMessage", "Job posted successfully!");

            event.target.reset();

            loadJobs();

        } catch (error) {
            showMessage("jobMessage", error.message, true);
        }
    }
);

// Load all jobs
async function loadJobs() {
    const container = document.getElementById("jobsList");

    container.replaceChildren();

    try {
        const jobs = await apiRequest(API + "/jobs");

        if (!jobs.length) {
            container.appendChild(
                createElement("p", "No jobs available yet.")
            );
            return;
        }

        jobs.forEach(function (job) {
            container.appendChild(createJobCard(job));
        });

    } catch (error) {
        container.appendChild(
            createElement("p", error.message, "error")
        );
    }
}

document.getElementById("loadJobsButton").addEventListener(
    "click",
    loadJobs
);

// Get job recommendations
document.getElementById("recommendationForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const userId =
            document.getElementById("recommendationUserId").value;

        const container =
            document.getElementById("recommendationsList");

        container.replaceChildren();

        try {
            const jobs = await apiRequest(
                API + "/matching/recommendations/" +
                encodeURIComponent(userId)
            );

            if (!jobs.length) {
                container.appendChild(
                    createElement("p", "No matching jobs found.")
                );
                return;
            }

            jobs.forEach(function (job) {
                container.appendChild(createJobCard(job, true));
            });

        } catch (error) {
            container.appendChild(
                createElement("p", error.message, "error")
            );
        }
    }
);

// Apply for a job
document.getElementById("applicationForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const userId =
            document.getElementById("applicationUserId").value;

        const jobId =
            document.getElementById("applicationJobId").value;

        try {
            await apiRequest(
                API + "/applications?userId=" +
                encodeURIComponent(userId) +
                "&jobId=" + encodeURIComponent(jobId),
                {
                    method: "POST"
                }
            );

            showMessage(
                "applicationMessage",
                "Your job application was submitted successfully."
            );

        } catch (error) {
            showMessage("applicationMessage", error.message, true);
        }
    }
);

// View user applications
document.getElementById("viewApplicationsForm").addEventListener(
    "submit",
    async function (event) {
        event.preventDefault();

        const userId =
            document.getElementById("viewApplicationsUserId").value;

        const container =
            document.getElementById("applicationsList");

        container.replaceChildren();

        try {
            const applications = await apiRequest(
                API + "/applications/user/" + encodeURIComponent(userId)
            );

            if (!applications.length) {
                container.appendChild(
                    createElement("p", "No applications found.")
                );
                return;
            }

            applications.forEach(function (application) {
                const card = createElement("div", "", "result-card");

                card.appendChild(
                    createElement(
                        "h3",
                        application.job
                            ? application.job.title
                            : "Job application"
                    )
                );

                card.appendChild(
                    createElement(
                        "p",
                        "Application ID: " + application.id
                    )
                );

                card.appendChild(
                    createElement(
                        "p",
                        "Status: " + application.status
                    )
                );

                card.appendChild(
                    createElement(
                        "p",
                        "Match Score: " + application.matchScore + "%"
                    )
                );

                container.appendChild(card);
            });

        } catch (error) {
            container.appendChild(
                createElement("p", error.message, "error")
            );
        }
    }
);