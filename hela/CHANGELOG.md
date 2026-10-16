# Changelog

## 0.2.0

- New: `HelaCalendar` (inline month grid) and `HelaDatePickerModal`, with `HelaDates` helpers.
  Dates are UTC-midnight epoch millis, matching Material's `DatePicker`.
- `HelaTextarea`: `keyboardOptions` and `keyboardActions`. Defaults to sentence capitalization.
- `HelaSelect`: `optionDescription` shows a line of explanation under each option in the menu.
- `HelaMenuItem`: optional `description` line under the label.
- `HelaBottomSheet`: stops below the status bar at full height, so `fillMaxHeight()` content no
  longer runs under it.

## 0.1.0

First release as a standalone library (`com.samkt:hela`), extracted from the sample app.

- Tokens: Geist color scales (light/dark), typography roles, spacing, radius, borders, motion.
- Theme: `HelaTheme` with a flat press indication instead of ripple, and Material mapping for leftovers.
- Components: Text, Icon (22 icons), Button, IconButton, Input, Textarea, Select, Checkbox, Radio,
  RadioGroup, Switch, SegmentedControl, Tabs, Menu, Collapse, Modal, BottomSheet, Toast, Note,
  Spinner, LoadingDots, Skeleton, Progress, Gauge, Card, CardFooter, Badge, StatusDot, Avatar,
  AvatarGroup, Snippet, Kbd, EmptyState, Divider.
- Previews: `@HelaPreviews` (light + dark) and `HelaPreviewSurface`.
- Package moved from `com.samkt.vercel.ui.hela` to `com.samkt.hela`.
