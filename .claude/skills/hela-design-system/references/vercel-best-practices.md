# Building Vercel-like apps

What makes an app feel like Vercel, beyond using the components. Use this for product decisions,
reviews, and anything not covered by a rule in SKILL.md.

## Contents
1. The aesthetic in one paragraph
2. Visual principles
3. Layout & density
4. Communicating state
5. Speed and perceived performance
6. Copywriting
7. Interaction details
8. Accessibility
9. Dark mode
10. Android-specific practices
11. Anti-patterns

---

## 1. The aesthetic in one paragraph

Vercel's interfaces are **quiet, precise and fast**. Most of the screen is black, white and a few
grays. The type is tight and confident, and hairline borders define the structure. Color appears only
when it means something, such as a red error or a green "Ready". There is no decoration: no
gradients on controls, no illustrations in the flow, no drop shadows on cards, no bouncy animation.
The content (deployments, logs, domains, numbers) is the interface. It is built for developers, so it
respects their time: it's dense without being cramped, keyboard-friendly, honest about state, and
never makes them wait without showing why.

## 2. Visual principles

- **Restraint is the brand.** When unsure, remove things. Use fewer colors, weights, sizes and borders.
- **Hierarchy through type and gray, not color.** Use `textPrimary` for what matters, `textSecondary`
  for context, and weight (`label14` medium) for emphasis.
- **One primary action per view.** The monochrome `Primary` button is the loudest element on the
  screen. Everything else is `Secondary`/`Tertiary`.
- **Hairline structure.** Use 1dp `border` lines and 12dp-radius cards. Shadows only on things that
  float (menus, toasts, modals, sheets).
- **Tight headings, relaxed body.** Negative tracking on headings is built into the type roles. Keep
  body text at comfortable line heights.
- **Mono for machine things.** IDs, hashes, commands, URLs in code, env vars, and table numbers.
- **Consistent geometry.** Use the 4dp grid, 6/8/12dp radii, and the same control heights everywhere
  (32/40/48dp). Small inconsistencies are what make an app feel "almost" like Vercel.

## 3. Layout & density

- On phones, use a single column with a 16dp gutter and a heading24 screen title. Place secondary
  actions at the top right of the title row.
- Group related settings into **cards**, one concern per card (Vercel settings). Each card has a
  title, one explanatory sentence, the control, and a footer with a hint and Save.
- Lists are **rows in a bordered container**, separated by hairlines: status → identifier →
  metadata → overflow. Rows are at least 56dp tall when tappable.
- Show what matters first: status and name, then metadata. Truncate long values with an ellipsis and
  `maxLines = 1`, and never wrap IDs.
- Use whitespace generously between sections (24 to 32dp) and tightly within a group (8 to 12dp).
  Density comes from tight groups, not from cramming sections together.
- On tablets, cap readable content at ~720dp wide and center it. Don't stretch forms to 1200dp.

## 4. Communicating state

Every async surface has four states. Design and preview all of them:

| State | Pattern |
|---|---|
| Loading | `HelaSkeleton` in the exact shape of the content. Use a spinner only inside buttons or for tiny inline areas. |
| Empty | `HelaEmptyState`: what this is, why it's empty, and **one** action to fix it. |
| Error | `HelaNote(type = Error)` with what happened in plain words, plus a Retry action. Keep stale data visible if you have it. |
| Content | The real thing. |

- **Statuses** (deploy, build, job) use `HelaStatusDot` with a label where space allows:
  Queued → Building → Ready / Error / Canceled. Keep the vocabulary consistent across the app.
- **Inline vs. transient:** a persistent condition ("Domain not verified") is a `HelaNote`. The
  result of something the user just did ("Copied", "Saved") is a toast. Never use a toast for errors
  the user must act on.
- **Destructive actions** use the `Error` variant, are confirmed in a `HelaModal` that names the
  resource and the consequence, and require typing the name for irreversible, high-impact deletes.
- **Usage and limits** use `HelaProgress` or `HelaGauge`, turning amber, then red, as limits approach.
  Always pair them with the number ("8.2 GB of 10 GB").
- **Disabled controls** should explain why, nearby (helper text or a note), when the reason isn't obvious.

## 5. Speed and perceived performance

Vercel is about speed, and the UI should feel instant.
- **Optimistic updates** for safe, reversible actions (toggle, rename, star): update the UI
  immediately, then roll back with an error toast if the request fails.
- **No layout shift.** Skeletons match the final size. Buttons keep their width while `loading`.
  Reserve space for helper/error text where a jump would be jarring.
- **Keep previous data while refreshing.** Don't flash a skeleton over content the user is already looking at.
- **Feedback within 100ms.** Every press shows the flat tint immediately (`HelaIndication` does this),
  and long operations show progress or a status.
- Animations are 150 to 300ms and never block input.

## 6. Copywriting

- **Sentence case** everywhere: "Create project", not "Create Project". Brand and proper nouns
  keep their case.
- **Short and specific.** "Deployment ready" beats "Your deployment has been successfully completed!"
- **Buttons are verbs that describe the outcome:** "Delete project", "Add domain", "Redeploy". Avoid
  "OK", "Submit" and "Yes".
- **Errors say what happened and what to do:** "Couldn't connect to GitHub. Check your
  permissions and try again." Don't use "Error 500" or "Something went wrong" alone, and never blame
  the user.
- **No exclamation marks, no filler** ("Please", "Simply", "Just"). Be calm and direct.
- **Numbers and time:** relative for recent ("2m ago", "3h ago") and absolute for older dates or on
  demand. Use units always ("12 ms", "1.2 GB"). Use mono for durations in tables.
- **Placeholders are examples, not instructions:** `placeholder = "my-app"`, with the instruction in
  `helper`.
- **Use consistent nouns** (project, deployment, domain, team) and never use synonyms for the same concept.

## 7. Interaction details

- Press feedback is a flat tint, not a ripple. It's already the default for any `clickable` under `HelaTheme`.
- Copyable things (URLs, IDs, commands) use `HelaSnippet` or show a copy action, and confirm with the
  icon turning into a check or a "Copied" toast.
- Overflow actions live in a `HelaMenu` (≤ 4 items) or a `HelaBottomSheet` (more, or on phones when
  thumbs matter). Destructive items go last, after a separator.
- Settings that apply instantly use `HelaSwitch`. Settings that need validation use a card with Save.
- Preserve user input across errors and configuration changes (`rememberSaveable` for form fields).
- Give keyboard and hardware-keyboard users a good experience: visible focus (`helaFocusRing`),
  logical focus order, and `ImeAction.Next`/`Done` on forms. Show shortcuts with `HelaKbd` where
  they exist (tablets, ChromeOS).

## 8. Accessibility

- Contrast: `textPrimary` and `textSecondary` on `surface` pass WCAG AA in both themes. `textDisabled`
  is intentionally low contrast, so never use it for information the user needs.
- Never carry meaning by color alone. Hela pairs color with icons (notes, toasts), dots plus labels
  (status), and an error dot plus text (inputs). Keep doing that in custom UI.
- Touch targets: ≥ 48dp for primary mobile actions (use `Large` buttons, 56dp rows). `Small` (32dp)
  controls belong in dense secondary contexts with spacing around them.
- Every icon-only control has a specific `contentDescription` ("More actions for acme-7f3k2", not
  "More"). Decorative icons next to text use `null`.
- Mark screen and section titles with `semantics { heading() }`. Errors are announced (`HelaInput`
  `error`, error notes, toasts are live regions).
- Respect font scaling: don't fix heights on text containers. Use `heightIn(min = …)`, and test at 200% font size.

## 9. Dark mode

- Dark mode is a first-class theme with true black backgrounds (`background100 = #000`), not
  an inverted afterthought. Check every screen's dark preview.
- Tokens swap automatically. Breakage almost always comes from a hard-coded color, a white-only
  image, or `Color.Black` text.
- In dark mode, borders do even more of the work, because shadows barely show on black. Keep hairlines on cards.
- Let the user override the system theme (the gallery's switch), and keep the status-bar icon style in
  sync (see SKILL.md setup).

## 10. Android-specific practices

- **Edge-to-edge:** draw behind the system bars and pad content with `WindowInsets.safeDrawing`
  (before `verticalScroll`). Sheets and toasts handle their own insets.
- **Back:** sheets, modals and menus close on back. Don't trap the user. Use `BackHandler` only to
  confirm discarding unsaved form changes.
- **Configuration changes:** hoist screen state into a ViewModel. Use `rememberSaveable` for UI-only
  state (selected tab, text being typed).
- **Lists:** use `LazyColumn` with stable `key`s for anything unbounded. Use a plain `Column` only
  for short, fixed lists.
- **Performance:** avoid reading fast-changing state high in the tree, pass lambdas for scroll-driven
  values, and keep previews and real screens on the same stateless composables.
- **Haptics:** use them sparingly. A light confirm on toggle or copy is fine. Never on every tap.

## 11. Anti-patterns (reject in review)

- Material components or ripples in feature code. Brand colors on buttons. Multiple primary buttons.
- Gradients, glows, colored card backgrounds, drop shadows on cards, rounded corners other than 6/8/12.
- Centered body text in content screens (centering is for empty states and onboarding only).
- Spinners for page loads where a skeleton would fit. Blank screens while loading.
- Toasts for errors that need action. Modals for non-blocking information.
- "Are you sure?" modals that don't name what will be lost.
- Title Case Buttons, exclamation marks, vague errors, and jargon the user didn't write.
- Hard-coded `Color(…)`, `sp` or `dp` values in screens. Ignoring dark mode or insets.
