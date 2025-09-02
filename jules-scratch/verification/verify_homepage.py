from playwright.sync_api import sync_playwright

def run(playwright):
    browser = playwright.chromium.launch()
    page = browser.new_page()
    page.goto("http://localhost:5173")
    page.wait_for_timeout(2000)  # Wait for 2 seconds for content to load
    page.screenshot(path="jules-scratch/verification/homepage.png")
    browser.close()

with sync_playwright() as playwright:
    run(playwright)
